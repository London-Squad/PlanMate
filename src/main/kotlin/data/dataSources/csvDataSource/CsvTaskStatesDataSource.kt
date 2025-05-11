package data.dataSources.csvDataSource

import data.dataSources.csvDataSource.fileIO.CsvFileHandler
import data.dataSources.csvDataSource.fileIO.CsvParser
import data.repositories.dtoMappers.toTaskState
import data.repositories.dtoMappers.toTaskStateDto
import logic.entities.TaskState
import logic.exceptions.TaskStateNotFoundException
import logic.repositories.TaskStatesRepository
import java.util.*

class CsvTaskStatesDataSource(
    private val tasksStatesCsvFileHandler: CsvFileHandler,
    private val csvParser: CsvParser,
) : TaskStatesRepository {

    override fun getTaskStatesByProjectId(projectId: UUID, includeDeleted: Boolean): List<TaskState> {
        return tasksStatesCsvFileHandler.readRecords().map(csvParser::recordToTaskStateDto)
            .filter { it.projectId == projectId }.filter { if (includeDeleted) true else !it.isDeleted }
            .map { it.toTaskState() }
    }

    override fun getTaskStateById(stateId: UUID, includeDeleted: Boolean): TaskState {
        return tasksStatesCsvFileHandler.readRecords().map(csvParser::recordToTaskStateDto)
            .filter { if (includeDeleted) true else !it.isDeleted }.firstOrNull { it.id == stateId }?.toTaskState()
            ?: throw TaskStateNotFoundException()
    }

    override fun addNewTaskState(taskState: TaskState, projectId: UUID) {
        tasksStatesCsvFileHandler.appendRecord(
            csvParser.taskStateDtoToRecord(taskState.toTaskStateDto(projectId))
        )
    }

    override fun editTaskStateTitle(stateId: UUID, newTitle: String) {
        tasksStatesCsvFileHandler.readRecords().map {
                val taskStateData = csvParser.recordToTaskStateDto(it)
                if (taskStateData.id == stateId) {
                    csvParser.taskStateDtoToRecord(taskStateData.copy(title = newTitle))
                } else it
            }.also(tasksStatesCsvFileHandler::rewriteRecords)
    }

    override fun editTaskStateDescription(stateId: UUID, newDescription: String) {
        tasksStatesCsvFileHandler.readRecords().map {
                val taskStateData = csvParser.recordToTaskStateDto(it)
                if (taskStateData.id == stateId) {
                    csvParser.taskStateDtoToRecord(taskStateData.copy(description = newDescription))
                } else it
            }.also(tasksStatesCsvFileHandler::rewriteRecords)
    }

    override fun deleteTaskState(stateId: UUID) {
        tasksStatesCsvFileHandler.readRecords().map {
                val taskStateData = csvParser.recordToTaskStateDto(it)
                if (taskStateData.id == stateId) {
                    csvParser.taskStateDtoToRecord(taskStateData.copy(isDeleted = true))
                } else it
            }.also(tasksStatesCsvFileHandler::rewriteRecords)


    }
}