package data.dataSources.csvDataSource

import data.dataSources.csvDataSource.fileIO.CsvFileHandler
import data.dataSources.csvDataSource.fileIO.CsvParser
import data.repositories.TasksDataSource
import data.dto.TaskDto
import java.util.*

class CsvTasksDataSource(
    private val tasksCsvFileHandler: CsvFileHandler,
    private val csvParser: CsvParser
) : TasksDataSource {

    override fun getAllTasks(includeDeleted: Boolean): List<TaskDto> {
        return tasksCsvFileHandler.readRecords()
            .map(csvParser::recordToTaskDto)
            .filter { if (includeDeleted) true else !it.isDeleted }
    }

    override fun addNewTask(taskDto: TaskDto) {
        tasksCsvFileHandler.appendRecord(
            csvParser.taskDtoToRecord(taskDto)
        )
    }

    override fun editTaskTitle(taskId: UUID, newTitle: String) {
        tasksCsvFileHandler.readRecords()
            .map {
                val taskData = csvParser.recordToTaskDto(it)
                if (taskData.id == taskId) {
                    csvParser.taskDtoToRecord(taskData.copy(title = newTitle))
                } else it
            }
            .also(tasksCsvFileHandler::rewriteRecords)
    }

    override fun editTaskDescription(taskId: UUID, newDescription: String) {
        tasksCsvFileHandler.readRecords()
            .map {
                val taskData = csvParser.recordToTaskDto(it)
                if (taskData.id == taskId) {
                    csvParser.taskDtoToRecord(taskData.copy(description = newDescription))
                } else it
            }
            .also(tasksCsvFileHandler::rewriteRecords)
    }

    override fun editTaskState(taskId: UUID, newStateId: UUID) {
        tasksCsvFileHandler.readRecords()
            .map {
                val taskData = csvParser.recordToTaskDto(it)
                if (taskData.id == taskId) {
                    csvParser.taskDtoToRecord(taskData.copy(stateId = newStateId))
                } else it
            }
            .also(tasksCsvFileHandler::rewriteRecords)
    }


    override fun deleteTask(taskId: UUID) {
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