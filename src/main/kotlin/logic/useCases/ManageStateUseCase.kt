package logic.useCases

import logic.entities.*
import logic.repositories.TaskStatesRepository
import java.util.*

class ManageStateUseCase(
    private val taskStatesRepository: TaskStatesRepository,
    private val createLogUseCase: CreateLogUseCase,
) {

    fun addState(title: String, description: String, projectId: UUID) {
        val taskState = buildNewTaskState(UUID.randomUUID(), title, description, projectId)

        taskStatesRepository.addNewTaskState(taskState, projectId)
        createLogUseCase.logEntityCreation(taskState.id)
    }

    private fun buildNewTaskState(
        id: UUID,
        title: String,
        description: String,
        projectId: UUID,
    ): TaskState {
        return TaskState(
            id = id,
            title = title,
            description = description,
            projectId = projectId,
        )
    }

    fun editStateTitle(stateId: UUID, newTitle: String) {
        val oldState = taskStatesRepository.getTaskStateById(stateId)

        taskStatesRepository.editTaskStateTitle(stateId, newTitle)
        createLogUseCase.logEntityTitleEdition(oldState.id, oldState.title, newTitle)
    }

    fun editStateDescription(stateId: UUID, newDescription: String) {
        val oldState = taskStatesRepository.getTaskStateById(stateId)

        taskStatesRepository.editTaskStateDescription(stateId, newDescription)
        createLogUseCase.logEntityDescriptionEdition(oldState.id, oldState.description, newDescription)
    }

    fun deleteState(stateId: UUID) {
        taskStatesRepository.deleteTaskState(stateId)
        createLogUseCase.logEntityDeletion(stateId)
    }

    fun getTaskStatesByProjectId(projectId: UUID): List<TaskState> {
        return taskStatesRepository.getTaskStatesByProjectId(projectId)
    }

    fun getTaskStatesById(stateId: UUID): TaskState {
        return taskStatesRepository.getTaskStateById(stateId)
    }
}