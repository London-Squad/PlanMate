package logic.useCases

import logic.repositories.ProjectsRepository
import logic.repositories.TaskRepository
import logic.repositories.TaskStatesRepository
import logic.repositories.UserRepository
import java.util.UUID

class GetEntityTitleUseCase(
    private val taskStateRepository: TaskStatesRepository,
    private val taskRepository: TaskRepository,
    private val projectRepository: ProjectsRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(entityId: UUID, entityType: String): String {
        return entityTitle(entityId, entityType)
    }

    private suspend fun entityTitle(entityId: UUID, entityType: String): String {
        return when (entityType.uppercase()) {
            "TASK_STATE" -> taskStateRepository.getTaskStateTitleById(entityId)
            "TASK" -> taskRepository.getTaskTitleById(entityId)
            "PROJECT" -> projectRepository.getProjectTitleById(entityId)
            "USER" -> userRepository.getUserNameById(entityId)
            else -> null
        } ?: "Unknown Entity Title"
    }
}
