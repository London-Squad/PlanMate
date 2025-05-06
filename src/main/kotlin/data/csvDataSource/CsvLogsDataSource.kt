package data.csvDataSource

import data.csvDataSource.fileIO.CsvFileHandler
import data.csvDataSource.fileIO.CsvParser
import data.dataSources.LogsDataSource
import data.dto.LogDto

class CsvLogsDataSource(
    private val logsCsvFileHandler: CsvFileHandler,
    private val csvParser: CsvParser
) : LogsDataSource {

    override fun getAllLogs(): List<LogDto> {
        return logsCsvFileHandler.readRecords()
            .map(csvParser::recordToLogDto)
    }

    override fun addLog(logDto: LogDto) {
        logsCsvFileHandler.appendRecord(
            csvParser.logDtoToRecord(logDto)
        )
    }
}

