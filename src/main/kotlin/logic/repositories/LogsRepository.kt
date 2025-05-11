package logic.repositories

import logic.entities.Log
import java.util.UUID

interface LogsRepository {
    fun getAllLogs(): List<Log>
    fun getLogsByEntityId(entityId: UUID): List<Log>
    fun addLog(log: Log)
}