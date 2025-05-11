package data.dataSources.csvDataSource

import data.dataSources.csvDataSource.fileIO.CsvFileHandler
import data.dataSources.csvDataSource.fileIO.CsvParser
import data.dto.TaskDto
import data.repositories.dtoMappers.toTask
import data.repositories.dtoMappers.toTaskDto
import logic.entities.Task
import logic.entities.TaskState
import logic.exceptions.TaskNotFoundException
import logic.repositories.TaskRepository
import java.util.*

class CsvTasksDataSource(
    private val tasksCsvFileHandler: CsvFileHandler,
    private val csvParser: CsvParser
) : TaskRepository {

    override fun getTasksByProjectID(
        projectId: UUID,
        includeDeleted: Boolean
    ): List<Task> {
        return tasksCsvFileHandler.readRecords()
            .map(csvParser::recordToTaskDto)
            .filter { it.projectId == projectId }
            .filter { if (includeDeleted) true else !it.isDeleted }
            .map(TaskDto::toTask)
    }

    override fun getTaskByID(taskId: UUID, includeDeleted: Boolean): Task {
        return tasksCsvFileHandler.readRecords()
            .map(csvParser::recordToTaskDto)
            .filter { if (includeDeleted) true else !it.isDeleted }
            .firstOrNull { it.id == taskId }
            ?.toTask()
            ?: throw TaskNotFoundException()

    }

    override fun addNewTask(task: Task, projectId: UUID) {
        tasksCsvFileHandler.appendRecord(
            csvParser.taskDtoToRecord(task.toTaskDto(projectId))
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

    override fun editTaskState(taskId: UUID, newTaskState: TaskState) {
        tasksCsvFileHandler.readRecords()
            .map {
                val taskData = csvParser.recordToTaskDto(it)
                if (taskData.id == taskId) {
                    csvParser.taskDtoToRecord(taskData.copy(stateId = newTaskState.id))
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