package logic.repositories

import logic.entities.Log
import java.util.UUID

interface LogsRepository {

    suspend fun getAllLogs(): List<Log>

    suspend fun getLogsByEntityId(entityId: UUID): List<Log>

    suspend fun addLog(log: Log)

}