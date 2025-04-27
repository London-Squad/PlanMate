package logic

import logic.entity.Log

interface LogsRepository {
    fun getAllLogs(): List<Log>
}