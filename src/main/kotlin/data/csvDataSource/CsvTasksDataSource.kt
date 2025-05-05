package data.csvDataSource

import data.csvDataSource.fileIO.CsvFileHandler
import data.csvDataSource.fileIO.Parser
import data.dataSources.TasksDataSource
import data.dto.TaskDto
import java.util.*

class CsvTasksDataSource(
    private val tasksCsvFileHandler: CsvFileHandler,
    private val parser: Parser
) : TasksDataSource {

    override fun getAllTasks(): List<TaskDto> {
        return tasksCsvFileHandler.readRecords()
            .map(parser::recordToTaskDto)
    }

    override fun addNewTask(taskDto: TaskDto) {
        tasksCsvFileHandler.appendRecord(
            parser.taskDtoToRecord(taskDto)
        )
    }

    override fun editTaskTitle(taskId: UUID, newTitle: String) {
        tasksCsvFileHandler.readRecords()
            .map {
                val taskData = parser.recordToTaskDto(it)
                if (taskData.id == taskId) {
                    parser.taskDtoToRecord(taskData.copy(title = newTitle))
                } else it
            }
            .also(tasksCsvFileHandler::rewriteRecords)
    }

    override fun editTaskDescription(taskId: UUID, newDescription: String) {
        tasksCsvFileHandler.readRecords()
            .map {
                val taskData = parser.recordToTaskDto(it)
                if (taskData.id == taskId) {
                    parser.taskDtoToRecord(taskData.copy(description = newDescription))
                } else it
            }
            .also(tasksCsvFileHandler::rewriteRecords)
    }

    override fun editTaskState(taskId: UUID, newStateId: UUID) {
        tasksCsvFileHandler.readRecords()
            .map {
                val taskData = parser.recordToTaskDto(it)
                if (taskData.id == taskId) {
                    parser.taskDtoToRecord(taskData.copy(stateId = newStateId))
                } else it
            }
            .also(tasksCsvFileHandler::rewriteRecords)
    }


    override fun deleteTask(taskId: UUID) {
        tasksCsvFileHandler.readRecords()
            .map {
                val taskData = parser.recordToTaskDto(it)
                if (taskData.id == taskId) {
                    parser.taskDtoToRecord(taskData.copy(isDeleted = true))
                } else it
            }
            .also(tasksCsvFileHandler::rewriteRecords)
    }
}