package data.dataSources.csvDataSource

import data.dataSources.csvDataSource.fileIO.CsvFileHandler
import data.dataSources.csvDataSource.fileIO.CsvParser
import data.dto.TaskStateDto
import data.repositories.dtoMappers.toTaskState
import data.repositories.dtoMappers.toTaskStateDto
import logic.entities.TaskState
import logic.exceptions.ProjectNotFoundException
import logic.exceptions.TaskStateNotFoundException
import logic.repositories.TaskStatesRepository
import java.util.*

class CsvTaskStatesDataSource(
    private val tasksStatesCsvFileHandler: CsvFileHandler,
    private val csvParser: CsvParser,
) : TaskStatesRepository {

    override suspend fun getTaskStatesByProjectId(projectId: UUID, includeDeleted: Boolean): List<TaskState> {
        return tasksStatesCsvFileHandler.readRecords().map(csvParser::recordToTaskStateDto)
            .filter { it.projectId == projectId.toString() }.filter { if (includeDeleted) true else !it.isDeleted }
            .map(TaskStateDto::toTaskState)
    }

    override suspend fun getTaskStateById(stateId: UUID, includeDeleted: Boolean): TaskState {
        return tasksStatesCsvFileHandler.readRecords().map(csvParser::recordToTaskStateDto)
            .filter { if (includeDeleted) true else !it.isDeleted }.firstOrNull { it.id == stateId.toString() }?.toTaskState()
            ?: throw TaskStateNotFoundException()
    }

    override suspend fun getTaskStateTitleById(stateId: UUID): String {
        return getTaskStateById(stateId,true).title
    }

    override suspend fun addNewTaskState(taskState: TaskState, projectId: UUID) {
        tasksStatesCsvFileHandler.appendRecord(
            csvParser.taskStateDtoToRecord(taskState.toTaskStateDto(projectId))
        )
    }

    override suspend fun editTaskStateTitle(stateId: UUID, newTitle: String) {
        var taskStateFound = false
        tasksStatesCsvFileHandler.readRecords().map {
            val taskStateData = csvParser.recordToTaskStateDto(it)
            if (taskStateData.id == stateId.toString()) {
                taskStateFound = true
                csvParser.taskStateDtoToRecord(taskStateData.copy(title = newTitle))
            } else it
        }.also {
            if (!taskStateFound) throw ProjectNotFoundException("Task state with ID $stateId not found")
            tasksStatesCsvFileHandler.rewriteRecords(it)
        }
    }

    override suspend fun editTaskStateDescription(stateId: UUID, newDescription: String) {
        var taskStateFound = false
        tasksStatesCsvFileHandler.readRecords().map {
            val taskStateData = csvParser.recordToTaskStateDto(it)
            if (taskStateData.id == stateId.toString()) {
                taskStateFound = true
                csvParser.taskStateDtoToRecord(taskStateData.copy(description = newDescription))
            } else it
        }.also {
            if (!taskStateFound) throw ProjectNotFoundException("Task state with ID $stateId not found")
            tasksStatesCsvFileHandler.rewriteRecords(it)
        }
    }

    override suspend fun deleteTaskState(stateId: UUID) {
        var taskStateFound = false
        tasksStatesCsvFileHandler.readRecords().map {
            val taskStateData = csvParser.recordToTaskStateDto(it)
            if (taskStateData.id == stateId.toString()) {
                taskStateFound = true
                csvParser.taskStateDtoToRecord(taskStateData.copy(isDeleted = true))
            } else it
        }.also {
            if (!taskStateFound) throw ProjectNotFoundException("Task state with ID $stateId not found")
            tasksStatesCsvFileHandler.rewriteRecords(it)
        }
    }
}