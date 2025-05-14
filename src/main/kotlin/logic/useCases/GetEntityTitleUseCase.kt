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
    suspend operator fun invoke(entityId: UUID): String? {
        return taskStateRepository.getTaskStateTitleById(entityId) ?: taskRepository.getTaskTitleById(entityId)
        ?: projectRepository.getProjectTitleById(entityId) ?: userRepository.getUserNameById(entityId)
        ?: "Unknown Entity Title"

    }
}