package logic.repositories

import logic.entities.State
import logic.entities.Task
import java.util.*

interface TaskRepository {

    fun getTasksByProjectID(projectId: UUID, includeDeleted: Boolean = false): List<Task>

    fun getTaskByID(taskId: UUID, includeDeleted: Boolean = false): Task

    fun addNewTask(task: Task, projectId: UUID)

    fun editTaskTitle(taskId: UUID, newTitle: String)

    fun editTaskDescription(taskId: UUID, newDescription: String)

    fun editTaskState(taskId: UUID, newState: State)

    fun deleteTask(taskId: UUID)
}