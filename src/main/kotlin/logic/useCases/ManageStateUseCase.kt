package logic.useCases

import logic.entities.*
import logic.repositories.AuthenticationRepository
import logic.repositories.LogsRepository
import logic.repositories.TasksStatesRepository
import java.time.LocalDateTime
import java.util.*

class ManageStateUseCase(
    private val tasksStatesRepository: TasksStatesRepository,
    private val logsRepository: LogsRepository,
    private val authenticationRepository: AuthenticationRepository
) {

    fun addState(state: State, projectID: UUID) {
        if (state.title.isBlank()) return
        tasksStatesRepository.addNewTaskState(state, projectID)
        logAction(Create(state))
    }

    fun editStateTitle(stateID: UUID, newTitle: String) {
        val oldState = tasksStatesRepository.getTaskStateById(stateID) ?: return
        if (newTitle.isBlank()) return
        tasksStatesRepository.editTaskStateTitle(stateID, newTitle)
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
        val oldState = tasksStatesRepository.getTaskStateById(stateID) ?: return
        if (newDescription.isBlank()) return
        tasksStatesRepository.editTaskStateDescription(stateID, newDescription)
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
        val oldState = tasksStatesRepository.getTaskStateById(stateID) ?: return
        if (oldState.id == State.NoState.id) return
        tasksStatesRepository.deleteTaskState(stateID)
        logAction(Delete(oldState))
    }

    fun getStates(projectId: UUID): List<State> {
        return tasksStatesRepository.getTasksStatesByProjectId(projectId)
    }

    private fun logAction(action: Action) {
        logsRepository.addLog(
            Log(
                user = authenticationRepository.getLoggedInUser(),
                time = LocalDateTime.now(),
                action = action
            )
        )
    }
}