package data.logsRepositories.csvFilesHandler

import data.logsRepositories.cvsFilesHandler.LogsCsvReader
import data.logsRepositories.cvsFilesHandler.LogsCsvWriter
import logic.entities.*
import logic.repositories.LogsRepository
import logic.repositories.ProjectsRepository
import logic.repositories.StatesRepository
import logic.repositories.TaskRepository
import java.time.LocalDateTime
import java.util.UUID

class LogsDataSource(
    private val logsCsvReader: LogsCsvReader,
    private val logsCsvWriter: LogsCsvWriter,
    private val projectsRepository: ProjectsRepository,
    private val statesRepository: StatesRepository,
    private val tasksRepository: TaskRepository,
) : LogsRepository {

    override fun getAllLogs(): List<Log> {
        return logsCsvReader.readRows().mapNotNull { parseLog(it) }
    }

    override fun getLogsByEntityId(entityId: UUID): List<Log> {
        var result: List<Log> = listOf()
        result = result + getAllLogs().filter { it.action.entity.id == entityId }

        result.forEach { log ->
            if (log.action.entity is Project) {
                (log.action.entity as Project).tasks.forEach { task ->
                    result = result + getLogsByEntityId(task.id)
                }
                (log.action.entity as Project).tasks.forEach { task ->
                    result = result + getLogsByEntityId(task.id)
                }
            }
        }

        return result
    }

    override fun addLog(log: Log) {
        val entityType = getEntityType(log.action.entity)
        val actionType = getActionType(log.action)
        val line = createLogLine(log, entityType, actionType)
        logsCsvWriter.appendLine(line)
    }

    private fun getEntityType(entity: PlanEntity): String {
        return when (entity) {
            is Task -> "Task"
            is Project -> "Project"
            is State -> "State"
            else -> throw (IllegalArgumentException("Unsupported entity type: ${entity::class.simpleName}"))
        }
    }

    private fun getActionType(action: Action): String {
        return when (action) {
            is Create -> "Create"
            is Delete -> "Delete"
            is Edit -> "Edit"
        }
    }

    private fun createLogLine(log: Log, entityType: String, actionType: String): String {
        var property: String = "Nan"
        var oldValue: String = "Nan"
        var newValue: String = "Nan"

        if (log.action is Edit) {
            property = log.action.property
            oldValue = log.action.oldValue
            newValue = log.action.newValue
        }

        return listOf(
            log.id.toString(),
            log.user.id.toString(),
            actionType,
            log.action.entity.id.toString(),
            entityType,
            log.time.toString(),
            property,
            oldValue,
            newValue
        ).joinToString(",")
    }

    private fun parseLog(line: String): Log? {
        val parts = line.split(",")

        val id = UUID.fromString(parts[0])
        val userId = UUID.fromString(parts[1])
        val actionType = parts[2]
        val entityId = UUID.fromString(parts[3])
        val entityType = parts[4]
        val timestamp = LocalDateTime.parse(parts[5])
        val property = parts[6]
        val oldValue = parts[7]
        val newValue = parts[8]

        val user = User(userId, "unknown", User.Type.MATE)
        val entity: PlanEntity = getEntityByType(entityType, entityId)

        val action: Action = getActionByType(
            actionType,
            entity,
            property,
            oldValue,
            newValue
        )

        return Log(id, user, timestamp, action)
    }

    private fun getEntityByType(entityType: String, entityId: UUID): PlanEntity {
        return when (entityType) {
            "Task" -> getTaskById(entityId)
            "Project" -> getProjectById(entityId)
            "State" -> getStateById(entityId)
            else -> Task(title = "unknown", description = "unknown")
        }
    }

    private fun getProjectById(projectId: UUID): Project {
        return projectsRepository.getAllProjects().first { it.id == projectId }
    }

    private fun getStateById(stateId: UUID): State {
        return statesRepository.getStateById(stateId)!!
    }

    private fun getTaskById(taskId: UUID): Task {
        return tasksRepository.getTaskById(taskId)!!
    }

    private fun getActionByType(
        actionType: String,
        entity: PlanEntity,
        property: String,
        oldValue: String,
        newValue: String
    ): Action {
        return when (actionType) {
            "Create" -> Create(entity)
            "Delete" -> Delete(entity)
            "Edit" -> Edit(entity, property, oldValue, newValue)
            else -> Create(entity)
        }
    }

    companion object {
        private const val EXPECTED_COLUMNS = 9
    }
}

