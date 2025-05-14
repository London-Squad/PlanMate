package data.dataSources.csvDataSource

import data.dataSources.csvDataSource.fileIO.CsvFileHandler
import data.dataSources.csvDataSource.fileIO.CsvParser
import data.dto.TaskDto
import data.repositories.dtoMappers.toTask
import data.repositories.dtoMappers.toTaskDto
import logic.entities.Task
import logic.exceptions.ProjectNotFoundException
import logic.exceptions.TaskNotFoundException
import logic.repositories.TaskRepository
import java.util.*

class CsvTasksDataSource(
    private val tasksCsvFileHandler: CsvFileHandler,
    private val csvParser: CsvParser
) : TaskRepository {


    override suspend fun getTasksByProjectID(projectId: UUID, includeDeleted: Boolean): List<Task> {
        return getAllTasks(includeDeleted)
            .filter { it.projectId == projectId.toString() }
            .map(TaskDto::toTask)
    }

    private fun getAllTasks(includeDeleted: Boolean): List<TaskDto> {
        return tasksCsvFileHandler.readRecords()
            .map(csvParser::recordToTaskDto)
            .filter { if (includeDeleted) true else !it.isDeleted }
    }

    override suspend fun getTaskByID(taskId: UUID, includeDeleted: Boolean): Task {
        return tasksCsvFileHandler.readRecords()
            .map(csvParser::recordToTaskDto)
            .filter { if (includeDeleted) true else !it.isDeleted }
            .firstOrNull { it.id == taskId.toString() }
            ?.toTask()
            ?: throw TaskNotFoundException()
    }

    override suspend fun getTaskTitleById(taskId: UUID): String {
        return getTaskByID(taskId,true).title
    }

    override suspend fun addNewTask(task: Task, projectId: UUID) {
        tasksCsvFileHandler.appendRecord(
            csvParser.taskDtoToRecord(task.toTaskDto(projectId))
        )
    }

    override suspend fun editTaskTitle(taskId: UUID, newTitle: String) {
        var taskFound = false
        tasksCsvFileHandler.readRecords()
            .map {
                val taskData = csvParser.recordToTaskDto(it)
                if (taskData.id == taskId.toString()) {
                    taskFound = true
                    csvParser.taskDtoToRecord(taskData.copy(title = newTitle))
                } else it
            }.also {
                if (!taskFound) throw ProjectNotFoundException("Task with ID $taskId not found")
            tasksCsvFileHandler.rewriteRecords(it)
            }
    }

    override suspend fun editTaskDescription(taskId: UUID, newDescription: String) {
        var taskFound = false
        tasksCsvFileHandler.readRecords()
            .map {
                val taskData = csvParser.recordToTaskDto(it)
                if (taskData.id == taskId.toString()) {
                    taskFound = true
                    csvParser.taskDtoToRecord(taskData.copy(description = newDescription))
                } else it
            }.also {
                if (!taskFound) throw ProjectNotFoundException("Task with ID $taskId not found")
            tasksCsvFileHandler.rewriteRecords(it)
            }
    }

    override suspend fun editTaskState(taskId: UUID, newStateId: UUID) {
        var taskFound = false
        tasksCsvFileHandler.readRecords()
            .map {
                val taskData = csvParser.recordToTaskDto(it)
                if (taskData.id == taskId.toString()) {
                    taskFound = true
                    csvParser.taskDtoToRecord(taskData.copy(stateId = newStateId.toString()))
                } else it
            }.also {
                if (!taskFound) throw ProjectNotFoundException("Task with ID $taskId not found")
            tasksCsvFileHandler.rewriteRecords(it)
            }
    }

    override suspend fun deleteTask(taskId: UUID) {
        var taskFound = false
        tasksCsvFileHandler.readRecords()
            .map {
                val taskData = csvParser.recordToTaskDto(it)
                if (taskData.id == taskId.toString()) {
                    taskFound = true
                    csvParser.taskDtoToRecord(taskData.copy(isDeleted = true))
                } else it
            }.also {
                if (!taskFound) throw ProjectNotFoundException("Task with ID $taskId not found")
            tasksCsvFileHandler.rewriteRecords(it)
            }
    }
}