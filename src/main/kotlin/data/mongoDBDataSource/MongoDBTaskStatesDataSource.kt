package data.mongoDBDataSource

import data.dataSources.TasksStatesDataSource
import data.dto.TaskStateDto
import java.util.*

class MongoDBTaskStatesDataSource : TasksStatesDataSource {

    override fun getAllTasksStates(): List<TaskStateDto> {
        TODO("Not yet implemented")
    }

    override fun addNewTaskState(taskStateDto: TaskStateDto) {
        TODO("Not yet implemented")
    }

    override fun editTaskStateTitle(stateId: UUID, newTitle: String) {
        TODO("Not yet implemented")
    }

    override fun editTaskStateDescription(stateId: UUID, newDescription: String) {
        TODO("Not yet implemented")
    }

    override fun deleteTaskState(stateId: UUID) {
        TODO("Not yet implemented")
    }

    companion object {
        private const val ID_FIELD = "id"
        private const val TITLE_FIELD = "title"
        private const val DESCRIPTION_FIELD = "description"
        private const val PROJECT_ID_FIELD = "projectId"
        private const val IS_DELETED_FIELD = "isDeleted"
    }
}