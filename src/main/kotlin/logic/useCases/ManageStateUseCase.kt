package logic.useCases

import logic.entities.*
import logic.repositories.TaskStatesRepository
import java.util.*

class ManageStateUseCase(
    private val taskStatesRepository: TaskStatesRepository,
    private val addLogUseCase: AddLogUseCase,
) {

    fun addState(title: String, description: String, projectID: UUID) {
        val taskState = buildNewTaskState(UUID.randomUUID(), title, description)

        taskStatesRepository.addNewTaskState(taskState, projectID)
        addLogUseCase.logEntityCreation(taskState)
    }

    private fun buildNewTaskState(
        id: UUID,
        title: String,
        description: String,
    ): TaskState {
        return TaskState(
            id = id,
            title = title,
            description = description,
        )
    }

    fun editStateTitle(stateID: UUID, newTitle: String) {
        val oldState = taskStatesRepository.getTaskStateById(stateID)

        taskStatesRepository.editTaskStateTitle(stateID, newTitle)
        addLogUseCase.logEntityTitleEdition(oldState, oldState.title, newTitle)
    }

    fun editStateDescription(stateID: UUID, newDescription: String) {
        val oldState = taskStatesRepository.getTaskStateById(stateID)

        taskStatesRepository.editTaskStateDescription(stateID, newDescription)
        addLogUseCase.logEntityDescriptionEdition(oldState, oldState.description, newDescription)
    }

    fun deleteState(stateID: UUID) {
        val oldState = taskStatesRepository.getTaskStateById(stateID)
        if (oldState.id == TaskState.NoTaskState.id) return

        taskStatesRepository.deleteTaskState(stateID)
        addLogUseCase.logEntityDeletion(oldState)
    }

    fun getTaskStatesByProjectId(projectId: UUID): List<TaskState> {
        return taskStatesRepository.getTaskStatesByProjectId(projectId)
    }
}