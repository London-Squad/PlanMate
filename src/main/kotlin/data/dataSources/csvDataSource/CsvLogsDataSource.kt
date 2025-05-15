package data.dataSources.csvDataSource

import data.dataSources.csvDataSource.fileIO.CsvFileHandler
import data.dataSources.csvDataSource.fileIO.CsvParser
import data.repositories.dtoMappers.toLog
import data.repositories.dtoMappers.toLogDto
import logic.entities.*
import logic.exceptions.ProjectNotFoundException
import logic.repositories.LogsRepository
import logic.repositories.ProjectsRepository
import logic.repositories.TaskRepository
import logic.repositories.TaskStatesRepository
import java.util.*

class CsvLogsDataSource(
    private val logsCsvFileHandler: CsvFileHandler,
    private val projectsRepository: ProjectsRepository,
    private val taskRepository: TaskRepository,
    private val taskStatesRepository: TaskStatesRepository,
    private val csvParser: CsvParser
) : LogsRepository {

    override suspend fun getAllLogs(): List<Log> {
        return logsCsvFileHandler.readRecords().map {
                csvParser.recordToLogDto(it).toLog()
            }
    }

    override suspend fun addLog(log: Log) {
        logsCsvFileHandler.appendRecord(
            csvParser.logDtoToRecord(log.toLogDto())
        )
    }

    override suspend fun getLogsByEntityId(entityId: MutableSet<String>): List<Log> {
        val allLogs = getAllLogs()
        var result = allLogs.filter { it.loggedAction.getEntityId() == entityId }
        val project: Project
        try {
            project = projectsRepository.getProjectById(entityId)
        } catch (e: ProjectNotFoundException) {
            return result.sortedBy { it.time }
        }

        val tasks = taskRepository.getTasksByProjectID(project.id, includeDeleted = true)
        val taskStates = taskStatesRepository.getTaskStatesByProjectId(project.id, includeDeleted = true)

        tasks.forEach { task ->
            result = result + allLogs.filter { it.loggedAction.getEntityId() == task.id }
        }

        taskStates.forEach { state ->
            result = result + allLogs.filter { it.loggedAction.getEntityId() == state.id }
        }

        return result.toSet().toList().sortedBy { it.time }
    }

    private fun LoggedAction.getEntityId(): UUID {
        return when (this) {
            is EntityCreationLog -> entityId
            is EntityDeletionLog -> entityId
            is EntityEditionLog -> entityId
        }
    }
}

