package logic.useCases

import logic.entities.Log
import logic.repositories.LogsRepository
import java.util.*

class GetLogsByEntityIdUseCase(
    private val logsRepository: LogsRepository
) {
    suspend fun getLogsByEntityId(entityId: UUID): List<Log> =
        logsRepository.getLogsByEntityId(entityId)
}