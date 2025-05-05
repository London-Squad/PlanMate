package data.mongoDBDataSource

import logic.entities.State
import logic.entities.Task
import logic.repositories.TaskRepository
import java.util.*

class MongoDBTasksDataSource() : TaskRepository {
    override fun getAllTasksByProjectID(projectId: UUID): List<Task> {
        TODO("Not yet implemented")
    }

    override fun getTaskByID(taskId: UUID): Task {
        TODO("Not yet implemented")
    }

    override fun addNewTask(task: Task, projectId: UUID) {
        TODO("Not yet implemented")
    }

    override fun editTaskTitle(taskId: UUID, newTitle: String) {
        TODO("Not yet implemented")
    }

    override fun editTaskDescription(taskId: UUID, newDescription: String) {
        TODO("Not yet implemented")
    }

    override fun editTaskState(taskId: UUID, newState: State) {
        TODO("Not yet implemented")
    }

    override fun deleteTask(taskId: UUID) {
        TODO("Not yet implemented")
    }

    companion object {
        private const val ID_FIELD = "id"
        private const val TITLE_FIELD = "title"
        private const val DESCRIPTION_FIELD = "description"
        private const val STATE_ID_FIELD = "stateId"
        private const val PROJECT_ID_FIELD = "projectId"
        private const val IS_DELETED_FIELD = "isDeleted"
    }
}