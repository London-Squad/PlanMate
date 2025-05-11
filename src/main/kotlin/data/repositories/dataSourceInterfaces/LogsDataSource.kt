package data.repositories.dataSourceInterfaces

import logic.entities.Log
import java.util.UUID

interface LogsDataSource {
    fun getAllLogs(): List<Log>
    fun addLog(log: Log)
    fun getLogsByEntityId(entityId: UUID): List<Log>
}