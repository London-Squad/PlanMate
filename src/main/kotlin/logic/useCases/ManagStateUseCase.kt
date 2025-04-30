package logic.useCases

import logic.entities.Action
import logic.entities.Create
import logic.entities.Delete
import logic.entities.Edit
import  logic.entities.State
import  logic.entities.User
import  logic.repositories.LogsRepository
import logic.repositories.StatesRepository
import java.util.*
import logic.entities.Log
import java.time.LocalDateTime



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

    private fun logAction(action: Action) {
        logsRepository.addLog(
            Log(
                user = activeUser,
                time = java.time.LocalDateTime.now(),
                action = action
            )
        )
    }
}


