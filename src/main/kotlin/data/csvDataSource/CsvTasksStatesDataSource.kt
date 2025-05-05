package data.csvDataSource

import data.csvDataSource.fileIO.CsvFileHandler
import data.csvDataSource.fileIO.Parser
import data.dataSources.TasksStatesDataSource
import data.entitiesData.TaskStateData
import logic.entities.State
import java.util.UUID

class CsvTasksStatesDataSource(
    private val tasksStatesCsvFileHandler: CsvFileHandler,
    private val parser: Parser
) : TasksStatesDataSource {

    override fun getAllTasksStates(): List<TaskStateData> {
        return tasksStatesCsvFileHandler.readRecords()
            .map(parser::recordToTaskStateData)
    }

    override fun addNewTaskState(taskStateData: TaskStateData) {
        tasksStatesCsvFileHandler.appendRecord(
            parser.taskStateDataToRecord(taskStateData)
        )
    }

    override fun editTaskStateTitle(stateId: UUID, newTitle: String) {
        tasksStatesCsvFileHandler.readRecords()
            .map {
                val newTaskData = parser.recordToTaskData(it)
                if (newTaskData.id == stateId) {
                    return@map parser.taskDataToRecord(newTaskData.copy(title = newTitle))
                }
                it
            }
            .also(tasksStatesCsvFileHandler::rewriteRecords)
    }

    override fun editTaskStateDescription(stateId: UUID, newDescription: String) {
        tasksStatesCsvFileHandler.readRecords()
            .map {
                val newTaskData = parser.recordToTaskData(it)
                if (newTaskData.id == stateId) {
                    return@map parser.taskDataToRecord(newTaskData.copy(description = newDescription))
                }
                it
            }
            .also(tasksStatesCsvFileHandler::rewriteRecords)
    }

    override fun deleteTaskState(stateId: UUID) {
        tasksStatesCsvFileHandler.readRecords()
            .map {
                val newTaskData = parser.recordToTaskData(it)
                if (newTaskData.id == stateId) {
                    return@map parser.taskDataToRecord(newTaskData.copy(isDeleted = true))
                }
                it
            }
            .also(tasksStatesCsvFileHandler::rewriteRecords)
    }
}