package data.csvDataSource

import data.csvDataSource.fileIO.CsvFileHandler
import data.csvDataSource.fileIO.Parser
import data.dataSources.TasksDataSource
import data.entitiesData.TaskData
import java.util.*

class CsvTasksDataSource(
    private val tasksCsvFileHandler: CsvFileHandler,
    private val parser: Parser
) : TasksDataSource {

    override fun getAllTasks(): List<TaskData> {
        return tasksCsvFileHandler.readRecords()
            .map(parser::recordToTaskData)
    }

    override fun addNewTask(taskData: TaskData) {
        tasksCsvFileHandler.appendRecord(
            parser.taskDataToRecord(taskData)
        )
    }

    override fun editTaskTitle(taskId: UUID, newTitle: String) {
        tasksCsvFileHandler.readRecords()
            .map {
                val taskData = parser.recordToTaskData(it)
                if (taskData.id == taskId) {
                    return@map parser.taskDataToRecord(taskData.copy(title = newTitle))
                }
                it
            }
            .also(tasksCsvFileHandler::rewriteRecords)
    }

    override fun editTaskDescription(taskId: UUID, newDescription: String) {
        tasksCsvFileHandler.readRecords()
            .map {
                val taskData = parser.recordToTaskData(it)
                if (taskData.id == taskId) {
                    return@map parser.taskDataToRecord(taskData.copy(description = newDescription))
                }
                it
            }
            .also(tasksCsvFileHandler::rewriteRecords)
    }

    override fun editTaskState(taskId: UUID, newStateId: UUID) {
        tasksCsvFileHandler.readRecords()
            .map {
                val taskData = parser.recordToTaskData(it)
                if (taskData.id == taskId) {
                    return@map parser.taskDataToRecord(taskData.copy(stateId = newStateId))
                }
                it
            }
            .also(tasksCsvFileHandler::rewriteRecords)
    }


    override fun deleteTask(taskId: UUID) {
        tasksCsvFileHandler.readRecords()
            .map {
                val taskData = parser.recordToTaskData(it)
                if (taskData.id == taskId) {
                    return@map parser.taskDataToRecord(taskData.copy(isDeleted = true))
                }
                it
            }
            .also(tasksCsvFileHandler::rewriteRecords)
    }
}