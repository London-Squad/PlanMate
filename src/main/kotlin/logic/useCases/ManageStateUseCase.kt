package logic.useCases

import logic.entities.Log.EntityType
import logic.entities.TaskState
import logic.repositories.TaskStatesRepository
import java.util.UUID

class ManageStateUseCase(
    private val taskStatesRepository: TaskStatesRepository,
    private val createLogUseCase: CreateLogUseCase,
) {

    suspend fun addState(title: String, description: String, projectId: UUID) {
        val taskState = buildNewTaskState(UUID.randomUUID(), title, description, projectId)

        taskStatesRepository.addNewTaskState(taskState, projectId)
        createLogUseCase.logEntityCreation(taskState.id, EntityType.TASK_STATE)
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

    suspend fun editStateTitle(stateId: UUID, newTitle: String) {
        val oldState = taskStatesRepository.getTaskStateById(stateId)

        taskStatesRepository.editTaskStateTitle(stateId, newTitle)
        createLogUseCase.logEntityTitleEdition(oldState.id, EntityType.TASK_STATE,oldState.title, newTitle)
    }

    suspend fun editStateDescription(stateId: UUID, newDescription: String) {
        val oldState = taskStatesRepository.getTaskStateById(stateId)

        taskStatesRepository.editTaskStateDescription(stateId, newDescription)
        createLogUseCase.logEntityDescriptionEdition(oldState.id, EntityType.TASK_STATE, oldState.description, newDescription)
    }

    suspend fun deleteState(stateId: UUID) {
        taskStatesRepository.deleteTaskState(stateId)
        createLogUseCase.logEntityDeletion(stateId, EntityType.TASK_STATE)
    }

    suspend fun getTaskStatesByProjectId(projectId: UUID): List<TaskState> {
        return taskStatesRepository.getTaskStatesByProjectId(projectId)
    }

    suspend fun getTaskStatesById(stateId: UUID): TaskState {
        return taskStatesRepository.getTaskStateById(stateId)
    }
}