package logic.useCases

import logic.entities.*
import logic.repositories.LogsRepository
import logic.repositories.StatesRepository
import java.time.LocalDateTime
import java.util.*

class ManageStateUseCase(
    private val statesRepository: StatesRepository,
    private val logsRepository: LogsRepository,
    private val activeUser: User
) {

    fun addState(state: State, projectID: UUID) {
        if (state.title.isBlank()) return
        statesRepository.addNewState(state, projectID)
        logAction(Create(state))
    }

    fun editStateTitle(stateID: UUID, newTitle: String) {
        val oldState = statesRepository.getStateById(stateID) ?: return
        if (newTitle.isBlank()) return
        statesRepository.editStateTitle(stateID, newTitle)
        logAction(
            Edit(
                entity = oldState.copy(title = newTitle),
                property = "title",
                oldValue = oldState.title,
                newValue = newTitle
            )
        )
    }

    fun editStateDescription(stateID: UUID, newDescription: String) {
        val oldState = statesRepository.getStateById(stateID) ?: return
        if (newDescription.isBlank()) return
        statesRepository.editStateDescription(stateID, newDescription)
        logAction(
            Edit(
                entity = oldState.copy(description = newDescription),
                property = "description",
                oldValue = oldState.description,
                newValue = newDescription
            )
        )
    }

    fun deleteState(stateID: UUID) {
        val oldState = statesRepository.getStateById(stateID) ?: return
        if (oldState.id == State.NoState.id) return
        statesRepository.deleteState(stateID)
        logAction(Delete(oldState))
    }

    fun getStates(projectId: UUID): List<State> {
        return statesRepository.getStatesByProjectId(projectId)
    }

    private fun logAction(action: Action) {
        logsRepository.addLog(
            Log(
                user = activeUser,
                time = LocalDateTime.now(),
                action = action
            )
        )
    }
}