package data.dataSources.csvDataSource

import data.dataSources.csvDataSource.fileIO.CsvFileHandler
import data.dataSources.csvDataSource.fileIO.CsvParser
import data.dto.LogDto
import data.repositories.dataSourceInterfaces.LogsDataSource

class CsvLogsDataSource(
    private val logsCsvFileHandler: CsvFileHandler,
    private val csvParser: CsvParser
) : LogsDataSource {

    override suspend fun getAllLogs(): List<LogDto> {
        return logsCsvFileHandler.readRecords()
            .map(csvParser::recordToLogDto)
    }

    override suspend fun addLog(logDto: LogDto) {
        logsCsvFileHandler.appendRecord(
            csvParser.logDtoToRecord(logDto)
        )
    }
}

