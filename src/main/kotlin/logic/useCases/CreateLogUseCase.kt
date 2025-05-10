package logic.useCases

import logic.entities.*
import logic.repositories.AuthenticationRepository
import logic.repositories.LogsRepository
import java.util.UUID

class CreateLogUseCase(
    private val logsRepository: LogsRepository,
    private val authenticationRepository: AuthenticationRepository,
) {

   suspend fun logEntityCreation(entityId: UUID) {
        logsRepository.addLog(
            Log(
                userId = authenticationRepository.getLoggedInUser().id,
                loggedAction = EntityCreationLog(entityId)
            )
        )
    }

   suspend fun logEntityTitleEdition(entityId: UUID, oldValue: String, newValue: String) {
        logsRepository.addLog(
            Log(
                userId = authenticationRepository.getLoggedInUser().id,
                loggedAction = EntityEditionLog(
                    entityId = entityId,
                    property = "title",
                    oldValue = oldValue,
                    newValue = newValue
                )
            )
        )
    }

   suspend fun logEntityDescriptionEdition(entityId: UUID, oldValue: String, newValue: String) {
        logsRepository.addLog(
            Log(
                userId = authenticationRepository.getLoggedInUser().id,
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
                loggedAction = EntityEditionLog(
                    entityId = taskId,
                    property = "state",
                    oldValue = oldState,
                    newValue = newState
                )
            )
        )
    }

   suspend fun logEntityDeletion(entityId: UUID) {
        logsRepository.addLog(
            Log(
                userId = authenticationRepository.getLoggedInUser().id,
                loggedAction = EntityDeletionLog(entityId)
            )
        )
    }
}
