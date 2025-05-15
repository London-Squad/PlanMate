package logic.repositories

import logic.entities.Log

interface LogsRepository {
    suspend fun getAllLogs(): List<Log>
    suspend fun getLogsByEntityId(entityId: MutableSet<String>): List<Log>
    suspend fun addLog(log: Log)
}