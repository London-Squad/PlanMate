package logic.repositories

import logic.entities.Log
import java.util.UUID

interface LogsRepository {

    fun getAllLogs(): List<Log>

    fun getLogById(id: UUID): List<Log>

    fun addLog(log: Log)

}