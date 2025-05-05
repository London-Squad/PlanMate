package data.csvDataSource

import data.csvDataSource.fileIO.CsvFileHandler
import data.csvDataSource.fileIO.Parser
import data.csvDataSource.fileIO.cvsLogsFileHandler.LogsCsvReader
import data.csvDataSource.fileIO.cvsLogsFileHandler.LogsCsvWriter
import data.dataSources.LogsDataSource
import data.entitiesData.LogData
import logic.entities.*
import logic.repositories.*
import java.time.LocalDateTime
import java.util.UUID

class CsvLogsDataSource(
    private val logsCsvFileHandler: CsvFileHandler,
    private val parser: Parser
) : LogsDataSource {

    override fun getAllLogs(): List<LogData> {
        return logsCsvFileHandler.readRecords()
            .map(parser::recordToLogData)
    }

    override fun addLog(logData: LogData) {
        logsCsvFileHandler.appendRecord(
            parser.logDataToRecord(logData)
        )
    }
}

