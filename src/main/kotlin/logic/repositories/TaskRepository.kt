package logic.repositories

import logic.entities.Task
import java.util.*

interface TaskRepository {

    fun addNewTask(task: Task, projectId: UUID)

    fun editTaskTitle(taskId: UUID, newTitle: String)

    fun editTaskDescription(taskId: UUID, newDescription: String)

    fun editTaskState(taskId: UUID, newStateId: UUID)

    fun deleteTask(taskId: UUID)
}