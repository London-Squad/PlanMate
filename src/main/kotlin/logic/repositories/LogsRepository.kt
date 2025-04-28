package logic.repositories

import logic.entities.Log

interface LogsRepository {

    fun getAllLogs(): List<Log>

    fun addLog(log: Log)

}