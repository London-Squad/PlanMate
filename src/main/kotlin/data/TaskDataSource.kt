package data

import logic.entities.Task
import logic.repositories.TaskRepository
import java.util.*

class TaskDataSource() : TaskRepository {
    override fun addNewTask(task: Task, projectId: UUID) {
        TODO("Not yet implemented")
    }

    override fun editTaskTitle(taskId: UUID, newTitle: String) {
        TODO("Not yet implemented")
    }

    override fun editTaskDescription(taskId: UUID, newDescription: String) {
        TODO("Not yet implemented")
    }

    override fun editTaskState(taskId: UUID, newStateId: UUID) {
        TODO("Not yet implemented")
    }

    override fun deleteTask(taskId: UUID) {
        TODO("Not yet implemented")
    }

    override fun getTaskById(taskId: UUID): Task? {
        TODO("Not yet implemented")
    }
}