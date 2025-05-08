package data.repositories

import data.dto.LogDto

interface LogsDataSource {
    fun getAllLogs(): List<LogDto>
    fun addLog(logDto: LogDto)
}