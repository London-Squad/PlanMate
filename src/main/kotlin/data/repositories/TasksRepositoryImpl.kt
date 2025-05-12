package data.repositories

import data.dto.TaskDto
import data.repositories.dataSourceInterfaces.TasksDataSource
import data.repositories.dtoMappers.toTask
import data.repositories.dtoMappers.toTaskDto
import logic.entities.Task
import logic.entities.TaskState
import logic.exceptions.TaskNotFoundException
import logic.repositories.TaskRepository
import java.util.*

class TasksRepositoryImpl(
    private val tasksDataSource: TasksDataSource,
) : TaskRepository {

    override suspend fun getTasksByProjectID(projectId: UUID, includeDeleted: Boolean): List<Task> {
        return tasksDataSource.getAllTasks(includeDeleted)
            .filter { it.projectId == projectId }
            .map(TaskDto::toTask)
    }

    override suspend fun getTaskByID(taskId: UUID, includeDeleted: Boolean): Task {
        return tasksDataSource.getAllTasks(includeDeleted)
            .filter { if (includeDeleted) true else !it.isDeleted }
            .firstOrNull { it.id == taskId }
            ?.toTask()
            ?: throw TaskNotFoundException()
    }

    override suspend fun addNewTask(task: Task, projectId: UUID) {
        tasksDataSource.addNewTask(
            task.toTaskDto(projectId)
        )
    }

    override suspend fun editTaskTitle(taskId: UUID, newTitle: String) {
        tasksDataSource.editTaskTitle(taskId, newTitle)
    }

    override suspend fun editTaskDescription(taskId: UUID, newDescription: String) {
        tasksDataSource.editTaskDescription(taskId, newDescription)
    }

    override suspend fun editTaskState(taskId: UUID, newTaskState: TaskState) {
        tasksDataSource.editTaskState(taskId, newTaskState.id)
    }

    override suspend fun deleteTask(taskId: UUID) {
        tasksDataSource.deleteTask(taskId)
    }
}