package data.repositories

import data.repositories.dataSourceInterfaces.LogsDataSource
import data.repositories.dataSourceInterfaces.TaskStatesDataSource
import data.repositories.dtoMappers.toLog
import data.repositories.dtoMappers.toLogDto
import logic.entities.*
import logic.exceptions.ProjectNotFoundException
import logic.repositories.LogsRepository
import logic.repositories.ProjectsRepository
import logic.repositories.TaskRepository
import java.util.*

class LogsRepositoryImpl(
    private val logsDataSource: LogsDataSource,
    private val projectsRepository: ProjectsRepository,
    private val taskStatesDataSource: TaskStatesDataSource,
    private val taskRepository: TaskRepository,
) : LogsRepository {
    override suspend fun getAllLogs(): List<Log> {
        return logsDataSource.getAllLogs()
            .map { it.toLog() }
    }


    override suspend fun getLogsByEntityId(entityId: UUID): List<Log> {
        val allLogs = getAllLogs()

        var result = allLogs.filter { it.loggedAction.getEntityId() == entityId }

        val project: Project
        try {
            project = projectsRepository.getProjectById(entityId)
        } catch (e: ProjectNotFoundException) {
            return result
        }

        val tasks = taskRepository.getTasksByProjectID(project.id, includeDeleted = true)
        val taskStates = taskStatesDataSource.getAllTasksStates(includeDeleted = true)
            .filter { it.projectId == project.id }

        tasks.forEach { task ->
            result = result + allLogs.filter { it.loggedAction.getEntityId() == task.id }
        }
        taskStates.forEach { state ->
            result = result + allLogs.filter { it.loggedAction.getEntityId() == state.id }
        }

        result = result.toSet().toList()

        result = result.sortedBy { it.time }

        return result
    }

    private fun LoggedAction.getEntityId(): UUID {
        return when (this) {
            is EntityCreationLog -> entityId
            is EntityDeletionLog -> entityId
            is EntityEditionLog -> entityId
        }
    }

    override suspend fun addLog(log: Log) {
        logsDataSource.addLog(log.toLogDto())
    }
}