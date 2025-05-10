package data.dataSources.csvDataSource

import data.dataSources.csvDataSource.fileIO.CsvFileHandler
import data.dataSources.csvDataSource.fileIO.CsvParser
import data.repositories.dataSourceInterfaces.TaskStatesDataSource
import data.dataSources.defaultTaskStatesTitleAndDescription
import data.dto.TaskStateDto
import java.util.UUID

class CsvTaskStatesDataSource(
    private val tasksStatesCsvFileHandler: CsvFileHandler,
    private val csvParser: CsvParser
) : TaskStatesDataSource {

    override fun getAllTasksStates(includeDeleted: Boolean): List<TaskStateDto> {
        return tasksStatesCsvFileHandler.readRecords()
            .map(csvParser::recordToTaskStateDto)
            .filter { if (includeDeleted) true else !it.isDeleted }
    }

    override fun addNewTaskState(taskStateDto: TaskStateDto) {
        tasksStatesCsvFileHandler.appendRecord(
            csvParser.taskStateDtoToRecord(taskStateDto)
        )
    }

    override fun editTaskStateTitle(stateId: UUID, newTitle: String) {
        tasksStatesCsvFileHandler.readRecords()
            .map {
                val newTaskData = csvParser.recordToTaskStateDto(it)
                if (newTaskData.id == stateId) {
                    csvParser.taskStateDtoToRecord(newTaskData.copy(title = newTitle))
                } else it
            }
            .also(tasksStatesCsvFileHandler::rewriteRecords)
    }

    override fun editTaskStateDescription(stateId: UUID, newDescription: String) {
        tasksStatesCsvFileHandler.readRecords()
            .map {
                val newTaskData = csvParser.recordToTaskStateDto(it)
                if (newTaskData.id == stateId) {
                    csvParser.taskStateDtoToRecord(newTaskData.copy(description = newDescription))
                } else it
            }
            .also(tasksStatesCsvFileHandler::rewriteRecords)
    }

    override fun deleteTaskState(stateId: UUID) {
        tasksStatesCsvFileHandler.readRecords()
            .map {
                val newTaskData = csvParser.recordToTaskStateDto(it)
                if (newTaskData.id == stateId) {
                    csvParser.taskStateDtoToRecord(newTaskData.copy(isDeleted = true))
                } else it
            }
            .also(tasksStatesCsvFileHandler::rewriteRecords)
    }
}