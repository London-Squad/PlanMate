package logic.repositories

import logic.entities.Task
import java.util.*

interface TaskRepository {
    suspend fun getTasksByProjectID(projectId: UUID, includeDeleted: Boolean = false): List<Task>
    suspend fun getTaskByID(taskId: UUID, includeDeleted: Boolean = false): Task
    suspend fun getTaskTitleById(taskId: UUID):String
    suspend fun addNewTask(task: Task, projectId: UUID)
    suspend fun editTaskTitle(taskId: UUID, newTitle: String)
    suspend fun editTaskDescription(taskId: UUID, newDescription: String)
    suspend fun editTaskState(taskId: UUID, newStateId: UUID)
    suspend fun deleteTask(taskId: UUID)
}