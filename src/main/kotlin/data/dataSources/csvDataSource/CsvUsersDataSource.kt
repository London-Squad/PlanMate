package data.dataSources.csvDataSource

import data.dataSources.csvDataSource.fileIO.CsvFileHandler
import data.dataSources.csvDataSource.fileIO.CsvParser
import data.dto.UserDto
import data.repositories.dataSourceInterfaces.UsersDataSource
import logic.entities.User
import logic.exceptions.NoLoggedInUserFoundException
import java.util.*

class CsvUsersDataSource(
    private val usersCsvFileHandler: CsvFileHandler,
    private val loggedInUserCsvFileHandler: CsvFileHandler,
    private val csvParser: CsvParser
) : UsersDataSource {
    private var loggedInUser: UserDto? = null

    init {
        loggedInUser = loadUserFromLocalFile()
    }

    override suspend fun getMates(): List<UserDto> {
        return usersCsvFileHandler.readRecords()
            .map(csvParser::recordToUserDto)
    }

    override suspend fun getAdmin(): UserDto = ADMIN

    override suspend fun deleteUser(userId: UUID) {
        usersCsvFileHandler.readRecords()
            .map {
                val userDto = csvParser.recordToUserDto(it)
                if (userDto.id == userId)
                    csvParser.userDtoToRecord(userDto.copy(isDeleted = true))
                else it
            }
            .also(usersCsvFileHandler::rewriteRecords)
    }

    override suspend fun addMate(userName: String, hashedPassword: String) {
        usersCsvFileHandler.appendRecord(
            UserDto(
                id = UUID.randomUUID(),
                userName = userName,
                hashedPassword = hashedPassword,
                type = User.Type.MATE.name,
                isDeleted = false
            ).let(csvParser::userDtoToRecord)
        )
    }

    override suspend fun getLoggedInUser(): UserDto {
        return loggedInUser ?: throw NoLoggedInUserFoundException()
    }

    override suspend fun setLoggedInUser(user: UserDto) {
        loggedInUserCsvFileHandler.rewriteRecords(
            listOf(csvParser.userDtoToRecord(user))
        )
        loggedInUser = user
    }

    override suspend fun clearLoggedInUser() {
        loggedInUserCsvFileHandler.rewriteRecords(
            listOf()
        )
        loggedInUser = null
    }

    private fun loadUserFromLocalFile(): UserDto? {
        return loggedInUserCsvFileHandler.readRecords()
            .takeIf { it.isNotEmpty() }
            ?.let { csvParser.recordToUserDto(it[0]) }
    }

    companion object {
        private val ADMIN = UserDto(
            id = UUID.fromString("5750f82c-c1b6-454d-b160-5b14857bc9dc"),
            userName = "admin",
            hashedPassword = "2e6e5a2b38ba905790605c9b101497bc",
            type = "ADMIN",
            isDeleted = false
        )
    }
}