package data.dataSources.csvDataSource

import data.dataSources.csvDataSource.fileIO.CsvFileHandler
import data.dataSources.csvDataSource.fileIO.CsvParser
import data.dto.TaskDto
import data.repositories.dataSourceInterfaces.TasksDataSource
import java.util.*

class CsvTasksDataSource(
    private val tasksCsvFileHandler: CsvFileHandler,
    private val csvParser: CsvParser
) : TasksDataSource {

    override suspend fun getAllTasks(includeDeleted: Boolean): List<TaskDto> {
        return tasksCsvFileHandler.readRecords()
            .map(csvParser::recordToTaskDto)
            .filter { if (includeDeleted) true else !it.isDeleted }
    }

    override suspend fun addNewTask(taskDto: TaskDto) {
        tasksCsvFileHandler.appendRecord(
            csvParser.taskDtoToRecord(taskDto)
        )
    }

    override suspend fun editTaskTitle(taskId: UUID, newTitle: String) {
        tasksCsvFileHandler.readRecords()
            .map {
                val taskData = csvParser.recordToTaskDto(it)
                if (taskData.id == taskId) {
                    csvParser.taskDtoToRecord(taskData.copy(title = newTitle))
                } else it
            }
            .also(tasksCsvFileHandler::rewriteRecords)
    }

    override suspend fun editTaskDescription(taskId: UUID, newDescription: String) {
        tasksCsvFileHandler.readRecords()
            .map {
                val taskData = csvParser.recordToTaskDto(it)
                if (taskData.id == taskId) {
                    csvParser.taskDtoToRecord(taskData.copy(description = newDescription))
                } else it
            }
            .also(tasksCsvFileHandler::rewriteRecords)
    }

    override suspend fun editTaskState(taskId: UUID, newStateId: UUID) {
        tasksCsvFileHandler.readRecords()
            .map {
                val taskData = csvParser.recordToTaskDto(it)
                if (taskData.id == taskId) {
                    csvParser.taskDtoToRecord(taskData.copy(stateId = newStateId))
                } else it
            }
            .also(tasksCsvFileHandler::rewriteRecords)
    }


    override suspend fun deleteTask(taskId: UUID) {
        tasksCsvFileHandler.readRecords()
            .map {
                val taskData = csvParser.recordToTaskDto(it)
                if (taskData.id == taskId) {
                    csvParser.taskDtoToRecord(taskData.copy(isDeleted = true))
                } else it
            }
            .also(tasksCsvFileHandler::rewriteRecords)
    }
}