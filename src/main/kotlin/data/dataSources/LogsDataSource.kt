package data.dataSources

import data.entitiesData.LogData

interface LogsDataSource {
    fun getAllLogs(): List<LogData>
    fun addLog(logData: LogData)
}