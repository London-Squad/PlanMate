package logic.useCases

import logic.entities.*
import logic.repositories.AuthenticationRepository
import logic.repositories.LogsRepository

class CreateLogUseCase(
    private val logsRepository: LogsRepository,
    private val authenticationRepository: AuthenticationRepository,
) {

    fun logEntityCreation(entity: PlanEntity) {
        logsRepository.addLog(
            Log(
                user = authenticationRepository.getLoggedInUser(),
                action = Create(entity)
            )
        )
    }

    fun logEntityTitleEdition(entity: PlanEntity, oldValue: String, newValue: String) {
        logsRepository.addLog(
            Log(
                user = authenticationRepository.getLoggedInUser(),
                action = Edit(
                    entity = entity,
                    property = "title",
                    oldValue = oldValue,
                    newValue = newValue
                )
            )
        )
    }

    fun logEntityDescriptionEdition(entity: PlanEntity, oldValue: String, newValue: String) {
        logsRepository.addLog(
            Log(
                user = authenticationRepository.getLoggedInUser(),
                action = Edit(
                    entity = entity,
                    property = "description",
                    oldValue = oldValue,
                    newValue = newValue
                )
            )
        )
    }

    fun logTaskStateEdition(task: Task, oldState: String, newState: String) {
        logsRepository.addLog(
            Log(
                user = authenticationRepository.getLoggedInUser(),
                action = Edit(
                    entity = task,
                    property = "state",
                    oldValue = oldState,
                    newValue = newState
                )
            )
        )
    }

    fun logEntityDeletion(entity: PlanEntity) {
        logsRepository.addLog(
            Log(
                user = authenticationRepository.getLoggedInUser(),
                action = Delete(entity)
            )
        )
    }
}
