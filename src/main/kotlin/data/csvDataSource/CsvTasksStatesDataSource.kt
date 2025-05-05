package data.csvDataSource

import data.csvDataSource.fileIO.CsvFileHandler
import data.csvDataSource.fileIO.Parser
import data.dataSources.TasksStatesDataSource
import data.dto.TaskStateDto
import java.util.UUID

class CsvTasksStatesDataSource(
    private val tasksStatesCsvFileHandler: CsvFileHandler,
    private val parser: Parser
) : TasksStatesDataSource {

    override fun getAllTasksStates(): List<TaskStateDto> {
        return tasksStatesCsvFileHandler.readRecords()
            .map(parser::recordToTaskStateDto)
    }

    override fun addNewTaskState(taskStateDto: TaskStateDto) {
        tasksStatesCsvFileHandler.appendRecord(
            parser.taskStateDtoToRecord(taskStateDto)
        )
    }

    override fun editTaskStateTitle(stateId: UUID, newTitle: String) {
        tasksStatesCsvFileHandler.readRecords()
            .map {
                val newTaskData = parser.recordToTaskStateDto(it)
                if (newTaskData.id == stateId) {
                    parser.taskStateDtoToRecord(newTaskData.copy(title = newTitle))
                } else it
            }
            .also(tasksStatesCsvFileHandler::rewriteRecords)
    }

    override fun editTaskStateDescription(stateId: UUID, newDescription: String) {
        tasksStatesCsvFileHandler.readRecords()
            .map {
                val newTaskData = parser.recordToTaskStateDto(it)
                if (newTaskData.id == stateId) {
                    parser.taskStateDtoToRecord(newTaskData.copy(description = newDescription))
                } else it
            }
            .also(tasksStatesCsvFileHandler::rewriteRecords)
    }

    override fun deleteTaskState(stateId: UUID) {
        tasksStatesCsvFileHandler.readRecords()
            .map {
                val newTaskData = parser.recordToTaskStateDto(it)
                if (newTaskData.id == stateId) {
                    parser.taskStateDtoToRecord(newTaskData.copy(isDeleted = true))
                } else it
            }
            .also(tasksStatesCsvFileHandler::rewriteRecords)
    }
}