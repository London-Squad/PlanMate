package logic.repositories

import logic.entities.Log
import java.util.*

interface LogsRepository {
    suspend fun getAllLogs(): List<Log>
    suspend fun getLogsByEntitiesIds(entityIdsSet: MutableSet<UUID>): List<Log>
    suspend fun addLog(log: Log)
}