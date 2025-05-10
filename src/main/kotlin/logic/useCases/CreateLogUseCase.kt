package logic.useCases

import logic.entities.*
import logic.repositories.AuthenticationRepository
import logic.repositories.LogsRepository

class CreateLogUseCase(
    private val logsRepository: LogsRepository,
    private val authenticationRepository: AuthenticationRepository,
) {

    suspend fun logEntityCreation(entity: PlanEntity) {
        logsRepository.addLog(
            Log(
                user = authenticationRepository.getLoggedInUser(),
                loggedAction = EntityCreationLog(entity)
            )
        )
    }

    suspend fun logEntityTitleEdition(entity: PlanEntity, oldValue: String, newValue: String) {
        logsRepository.addLog(
            Log(
                user = authenticationRepository.getLoggedInUser(),
                loggedAction = EntityEditionLog(
                    entity = entity,
                    property = "title",
                    oldValue = oldValue,
                    newValue = newValue
                )
            )
        )
    }

    suspend fun logEntityDescriptionEdition(entity: PlanEntity, oldValue: String, newValue: String) {
        logsRepository.addLog(
            Log(
                user = authenticationRepository.getLoggedInUser(),
                loggedAction = EntityEditionLog(
                    entity = entity,
                    property = "description",
                    oldValue = oldValue,
                    newValue = newValue
                )
            )
        )
    }

    suspend fun logTaskStateEdition(task: Task, oldState: String, newState: String) {
        logsRepository.addLog(
            Log(
                user = authenticationRepository.getLoggedInUser(),
                loggedAction = EntityEditionLog(
                    entity = task,
                    property = "state",
                    oldValue = oldState,
                    newValue = newState
                )
            )
        )
    }

    suspend fun logEntityDeletion(entity: PlanEntity) {
        logsRepository.addLog(
            Log(
                user = authenticationRepository.getLoggedInUser(),
                loggedAction = EntityDeletionLog(entity)
            )
        )
    }
}
