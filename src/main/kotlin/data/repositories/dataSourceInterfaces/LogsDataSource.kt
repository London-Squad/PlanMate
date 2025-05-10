package data.repositories.dataSourceInterfaces

import data.dto.LogDto

interface LogsDataSource {
    suspend fun getAllLogs(): List<LogDto>
    suspend fun addLog(logDto: LogDto)
}