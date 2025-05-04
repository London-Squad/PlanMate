package logic.useCases

import logic.entities.*
import logic.repositories.LogsRepository
import logic.repositories.StatesRepository
import java.util.UUID

class ManageStateUseCase(
    private val statesRepository: StatesRepository,
    private val logsRepository: LogsRepository,
) {

    fun addState(state: State, projectID: UUID) {
        statesRepository.addNewState(state, projectID)
        logsRepository.addCreationLog(state)
    }

    fun editStateTitle(stateId: UUID, newTitle: String) {
        val state = statesRepository.getStateById(stateId)

        statesRepository.editStateTitle(stateId, newTitle)

        logsRepository.addEditionLog(
            state,
            "title",
            state.title,
            newTitle

        )
    }

    fun editStateDescription(stateId: UUID, newDescription: String) {

        val state = statesRepository.getStateById(stateId)

        statesRepository.editStateTitle(stateId, newDescription)

        logsRepository.addEditionLog(
            state,
            "description",
            state.description,
            newDescription

        )
    }

    fun deleteState(stateId: UUID) {
        val state = statesRepository.getStateById(stateId)
        logsRepository.addDeletionLog(state)
        statesRepository.deleteState(stateId)
    }

    fun getStates(projectId: UUID): List<State> {
        return statesRepository.getAllStatesByProjectId(projectId)
    }
}