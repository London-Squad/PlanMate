package logic

import model.Log

interface LogsRepository {
    fun getAllLogs(): List<Log>
}