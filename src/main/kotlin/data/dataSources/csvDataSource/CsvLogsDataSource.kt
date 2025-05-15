package data.dataSources.csvDataSource

import data.dataSources.csvDataSource.fileIO.CsvFileHandler
import data.dataSources.csvDataSource.fileIO.CsvParser
import data.dto.LogDto
import data.repositories.dtoMappers.toLog
import data.repositories.dtoMappers.toLogDto
import logic.entities.Log
import logic.repositories.LogsRepository
import java.util.*

class CsvLogsDataSource(
    private val logsCsvFileHandler: CsvFileHandler,
    private val csvParser: CsvParser
) : LogsRepository {

    override suspend fun getAllLogs(): List<Log> {
        return logsCsvFileHandler.readRecords().map {
                csvParser.recordToLogDto(it).toLog()
            }
    }

    override suspend fun addLog(log: Log) {
        logsCsvFileHandler.appendRecord(
            csvParser.logDtoToRecord(log.toLogDto())
        )
    }

    override suspend fun getLogsByEntitiesIds(entityIdsSet: MutableSet<UUID>): List<Log> {
        val allLogs = logsCsvFileHandler.readRecords().map {
            csvParser.recordToLogDto(it)
        }
        val result = allLogs.filter { it.planEntityId in entityIdsSet.map{it.toString()} }

        return result.map(LogDto::toLog).sortedBy { it.time }
    }
}

