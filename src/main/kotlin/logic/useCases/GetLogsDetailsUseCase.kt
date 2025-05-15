package logic.useCases

import logic.entities.Log
import logic.entities.Log.EntityType
import logic.exceptions.NotFoundException
import logic.repositories.*
import java.util.*


class GetLogsDetailsUseCase(
    private val logsRepository: LogsRepository,
    private val taskStateRepository: TaskStatesRepository,
    private val taskRepository: TaskRepository,
    private val projectRepository: ProjectsRepository,
    private val userRepository: UserRepository
) {
    suspend fun getLogsByEntityId(entityId: UUID): List<Log> {
        val relatedEntityIds = mutableSetOf(entityId)

        return try {
            relatedEntityIds.apply {
                projectRepository.getProjectById(entityId).let { project ->
                    addAll(taskRepository.getTasksByProjectID(project.id, includeDeleted = true).map { it.id })
                    addAll(
                        taskStateRepository.getTaskStatesByProjectId(project.id, includeDeleted = true).map { it.id })
                }
            }
            logsRepository.getLogsByEntitiesIds(relatedEntityIds)
        } catch (e: NotFoundException) {
            logsRepository.getLogsByEntitiesIds(relatedEntityIds)
        }
    }

    suspend fun getEntityTitleById(entityId: UUID, entityType: EntityType): String {
        return when (entityType) {
            EntityType.TASK_STATE -> taskStateRepository.getTaskStateTitleById(entityId)
            EntityType.TASK -> taskRepository.getTaskTitleById(entityId)
            EntityType.PROJECT -> projectRepository.getProjectTitleById(entityId)
            EntityType.USER -> userRepository.getUserNameById(entityId)
        }
    }
}
