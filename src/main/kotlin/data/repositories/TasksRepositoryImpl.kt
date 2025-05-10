package data.repositories

import data.dto.TaskDto
import data.repositories.dataSourceInterfaces.TasksDataSource
import data.repositories.dtoMappers.toTask
import data.repositories.dtoMappers.toTaskDto
import logic.entities.TaskState
import logic.entities.Task
import logic.exceptions.TaskNotFoundException
import logic.repositories.TaskRepository
import java.util.*

class TasksRepositoryImpl(
    private val tasksDataSource: TasksDataSource,
) : TaskRepository {

    override fun getTasksByProjectID(projectId: UUID, includeDeleted: Boolean): List<Task> {
        return tasksDataSource.getAllTasks(includeDeleted)
            .filter { it.projectId == projectId }
            .map(TaskDto::toTask)
    }

    override fun getTaskByID(taskId: UUID, includeDeleted: Boolean): Task {
        return tasksDataSource.getAllTasks(includeDeleted)
            .filter { if (includeDeleted) true else !it.isDeleted }
            .firstOrNull { it.id == taskId }
            ?.toTask()
            ?: throw TaskNotFoundException()
    }

    override fun addNewTask(task: Task, projectId: UUID) {
        tasksDataSource.addNewTask(
            task.toTaskDto(projectId)
        )
    }

    override fun editTaskTitle(taskId: UUID, newTitle: String) {
        tasksDataSource.editTaskTitle(taskId, newTitle)
    }

    override fun editTaskDescription(taskId: UUID, newDescription: String) {
        tasksDataSource.editTaskDescription(taskId, newDescription)
    }

    override fun editTaskState(taskId: UUID, newTaskState: TaskState) {
        tasksDataSource.editTaskState(taskId, newTaskState.id)
    }

    override fun deleteTask(taskId: UUID) {
        tasksDataSource.deleteTask(taskId)
    }
}