package data.csvDataSource

import data.csvDataSource.fileIO.CsvFileHandler
import data.csvDataSource.fileIO.Parser
import data.dataSources.LogsDataSource
import data.dto.LogDto

class CsvLogsDataSource(
    private val logsCsvFileHandler: CsvFileHandler,
    private val parser: Parser
) : LogsDataSource {

    override fun getAllLogs(): List<LogDto> {
        return logsCsvFileHandler.readRecords()
            .map(parser::recordToLogDto)
    }

    override fun addLog(logDto: LogDto) {
        logsCsvFileHandler.appendRecord(
            parser.logDtoToRecord(logDto)
        )
    }
}

