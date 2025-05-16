package data.dataSources.csvDataSource

import data.dataSources.csvDataSource.fileIO.CsvFileHandler
import data.dataSources.csvDataSource.fileIO.CsvParser
import data.dto.UserCsvDto
import data.repositories.dataSources.LoggedInUserCacheDataSource
import logic.exceptions.NoLoggedInUserFoundException

class CsvLoggedInUserCacheDataSource(
    private val loggedInUserCsvFileHandler: CsvFileHandler,
    private val csvParser: CsvParser
) : LoggedInUserCacheDataSource {
    private var loggedInUser: UserCsvDto? = null

    init {
        loggedInUser = loadUserFromLocalFile()
    }

    override fun getLoggedInUser(): UserCsvDto {
        return loggedInUser ?: throw NoLoggedInUserFoundException()
    }

    override fun setLoggedInUser(user: UserCsvDto) {
        loggedInUserCsvFileHandler.rewriteRecords(
            listOf(csvParser.userDtoToRecord(user))
        )
        loggedInUser = user
    }

    override fun clearLoggedInUser() {
        loggedInUserCsvFileHandler.rewriteRecords(
            listOf()
        )
        loggedInUser = null
    }

    private fun loadUserFromLocalFile(): UserCsvDto? {
        return loggedInUserCsvFileHandler.readRecords()
            .takeIf { it.isNotEmpty() }
            ?.let { csvParser.recordToUserDto(it[0]) }
    }
}