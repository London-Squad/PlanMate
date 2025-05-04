package logic.repositories

import logic.entities.State
import logic.entities.Task
import java.util.*

interface TaskRepository {

    fun getAllTasksByProjectID(projectId: UUID): List<Task>

    fun getTaskByID(taskId: UUID): Task

    fun addNewTask(task: Task, projectId: UUID)

    fun editTaskTitle(taskId: UUID, newTitle: String)

    fun editTaskDescription(taskId: UUID, newDescription: String)

    fun editTaskState(taskId: UUID, newState: State)

    fun deleteTask(taskId: UUID)
}