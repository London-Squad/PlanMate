package data.logsRepositories.csvFilesHandler

import data.logsRepositories.cvsFilesHandler.CsvReader
import data.logsRepositories.cvsFilesHandler.CsvWriter
import data.logsRepositories.cvsFilesHandler.FilePaths
import logic.entities.*
import logic.repositories.LogsRepository
import java.time.LocalDateTime
import java.util.UUID

class LogsRepositoryImpl(
    private val csvReader: CsvReader,
    private val csvWriter: CsvWriter,
    private val filePath: String = FilePaths.allLogsFile
) : LogsRepository {

    override fun getAllLogs(): List<Log> {
        return csvReader.readLines(filePath).mapNotNull { parseLog(it) }
    }

    override fun getLogsByEntityId(entityId: UUID): List<Log> {
        return getAllLogs().filter { it.action.entity.id == entityId }
    }

    override fun addLog(log: Log) {
        val entityType = getEntityType(log.action.entity)
        val actionType = getActionType(log.action)
        val line = createLogLine(log, entityType, actionType)
        csvWriter.appendLine(filePath, line)
    }

    private fun getEntityType(entity: PlanEntity): String {
        return when (entity) {
            is Task -> "Task"
            is Project -> "Project"
            is State -> "State"
            else -> throw( IllegalArgumentException("Unsupported entity type: ${entity::class.simpleName}"))
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
        return listOf(
            log.id.toString(),
            log.user.id.toString(),
            log.time.toString(),
            log.action.entity.id.toString(),
            entityType,
            actionType
        ).joinToString(",")
    }

    private  fun parseLog(line: String): Log? {
        val parts = line.split(",")
        if (parts.size < EXPECTED_COLUMNS) {
            println("Invalid log line, skipping: $line")
            return null
        }

        return try {
            val id = UUID.fromString(parts[0])
            val userId = UUID.fromString(parts[1])
            val timestamp = LocalDateTime.parse(parts[2])
            val entityId = UUID.fromString(parts[3])
            val entityType = parts[4]
            val actionType = parts[5]

            val user = User(userId, "unknown", User.Type.MATE)
            val entity: PlanEntity = getEntityByType(entityType, entityId)
                ?: return null

            val action: Action = getActionByType(actionType, entity)
                ?: return null

            Log(id, user, timestamp, action)
        } catch (e: Exception) {
            println("Error parsing log line: $line")
            null
        }
    }

    private  fun getEntityByType(entityType: String, entityId: UUID): PlanEntity? {
        return when (entityType) {
            "Task" -> Task(entityId, "unknown", "unknown")
            "Project" -> Project(entityId, "unknown", "unknown", emptyList(), emptyList())
            "State" -> State(entityId, "unknown", "unknown")
            else -> null
        }
    }

    private  fun getActionByType(actionType: String, entity: PlanEntity): Action? {
        return when (actionType) {
            "Create" -> Create(entity)
            "Delete" -> Delete(entity)
            "Edit" -> Edit(entity, "unknown", "unknown", "unknown")
            else -> null
        }
    }

    companion object {
        private const val EXPECTED_COLUMNS = 6
    }
}

