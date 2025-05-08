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

    fun addState(taskState: TaskState, projectID: UUID) {
        if (taskState.title.isBlank()) return
        tasksStatesRepository.addNewTaskState(taskState, projectID)
        logAction(LogCreate(taskState))
    }

    fun editStateTitle(stateID: UUID, newTitle: String) {
        val oldState = tasksStatesRepository.getTaskStateById(stateID) ?: return
        if (newTitle.isBlank()) return
        tasksStatesRepository.editTaskStateTitle(stateID, newTitle)
        logAction(
            LogEdit(
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
            LogEdit(
                entity = oldState.copy(description = newDescription),
                property = "description",
                oldValue = oldState.description,
                newValue = newDescription
            )
        )
    }

    fun deleteState(stateID: UUID) {
        val oldState = tasksStatesRepository.getTaskStateById(stateID) ?: return
        if (oldState.id == TaskState.NoTaskState.id) return
        tasksStatesRepository.deleteTaskState(stateID)
        logAction(LogDelete(oldState))
    }

    fun getStates(projectId: UUID): List<TaskState> {
        return tasksStatesRepository.getTasksStatesByProjectId(projectId)
    }

    private fun logAction(action: LogEntry) {
        logsRepository.addLog(
            Log(
                user = authenticationRepository.getLoggedInUser(),
                time = LocalDateTime.now(),
                logEntry = action
            )
        )
    }
}