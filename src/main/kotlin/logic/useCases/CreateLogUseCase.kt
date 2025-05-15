package logic.useCases

import logic.entities.EntityCreationLog
import logic.entities.EntityDeletionLog
import logic.entities.EntityEditionLog
import logic.entities.Log
import logic.entities.Log.EntityType
import logic.repositories.AuthenticationRepository
import logic.repositories.LogsRepository
import java.util.UUID

class CreateLogUseCase(
    private val logsRepository: LogsRepository,
    private val authenticationRepository: AuthenticationRepository,
) {

    suspend fun logEntityCreation(entityId: UUID, entityType: EntityType) {
        logsRepository.addLog(
            Log(
                userId = authenticationRepository.getLoggedInUser().id,
                entityType = entityType,
                loggedAction = EntityCreationLog(entityId)
            )
        )
    }

    suspend fun logEntityTitleEdition(entityId: UUID, entityType: EntityType, oldValue: String, newValue: String) {
        logsRepository.addLog(
            Log(
                userId = authenticationRepository.getLoggedInUser().id,
                entityType = entityType,
                loggedAction = EntityEditionLog(
                    entityId = entityId,
                    property = "title",
                    oldValue = oldValue,
                    newValue = newValue
                )
            )
        )
    }

    suspend fun logEntityDescriptionEdition(entityId: UUID, entityType: EntityType, oldValue: String, newValue: String) {
        logsRepository.addLog(
            Log(
                userId = authenticationRepository.getLoggedInUser().id,
                entityType = entityType,
                loggedAction = EntityEditionLog(
                    entityId = entityId,
                    property = "description",
                    oldValue = oldValue,
                    newValue = newValue
                )
            )
        )
    }

    suspend fun logTaskStateEdition(taskId: UUID, oldState: String, newState: String) {
        logsRepository.addLog(
            Log(
                userId = authenticationRepository.getLoggedInUser().id,
                entityType = EntityType.TASK,
                loggedAction = EntityEditionLog(
                    entityId = taskId,
                    property = "state",
                    oldValue = oldState,
                    newValue = newState
                )
            )
        )
    }

    suspend fun logEntityDeletion(entityId: UUID, entityType: EntityType) {
        logsRepository.addLog(
            Log(
                userId = authenticationRepository.getLoggedInUser().id,
                entityType = entityType,
                loggedAction = EntityDeletionLog(entityId)
            )
        )
    }
}
