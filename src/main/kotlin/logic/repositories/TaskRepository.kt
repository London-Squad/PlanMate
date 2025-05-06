package logic.repositories

import logic.entities.TaskState
import logic.entities.Task
import java.util.*

interface TaskRepository {

    fun getTasksByProjectID(projectId: UUID, includeDeleted: Boolean = false): List<Task>

    fun getTaskByID(taskId: UUID, includeDeleted: Boolean = false): Task

    fun addNewTask(task: Task, projectId: UUID)

    fun editTaskTitle(taskId: UUID, newTitle: String)

    fun editTaskDescription(taskId: UUID, newDescription: String)

    fun editTaskState(taskId: UUID, newTaskState: TaskState)

    fun deleteTask(taskId: UUID)
}