package data.mongoDBDataSource

import logic.entities.State
import logic.repositories.StatesRepository
import java.util.*

class MongoDBTaskStatesDataSource() : StatesRepository {
    override fun getAllStatesByProjectId(projectId: UUID): List<State> {
        TODO("Not yet implemented")
    }

    override fun getStateById(stateId: UUID): State {
        TODO("Not yet implemented")
    }

    override fun addNewState(state: State, projectId: UUID) {
        TODO("Not yet implemented")
    }

    override fun editStateTitle(stateId: UUID, newTitle: String) {
        TODO("Not yet implemented")
    }

    override fun editStateDescription(stateId: UUID, newDescription: String) {
        TODO("Not yet implemented")
    }

    override fun deleteState(stateId: UUID) {
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