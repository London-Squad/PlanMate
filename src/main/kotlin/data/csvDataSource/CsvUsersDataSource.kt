package data.csvDataSource

import data.csvDataSource.fileIO.CsvFileHandler
import data.csvDataSource.fileIO.Parser
import data.dataSources.UsersDataSource
import data.dto.UserDto
import logic.entities.User
import logic.exceptions.NoLoggedInUserIsSavedInCacheException
import java.util.*

class CsvUsersDataSource(
    private val usersCsvFileHandler: CsvFileHandler,
    private val loggedInUserCsvFileHandler: CsvFileHandler,
    private val parser: Parser
) : UsersDataSource {
    private var loggedInUser: UserDto? = null

    init {
        loggedInUser = loadUserFromLocalFile()
    }

    override fun getMates(): List<UserDto> {
        return usersCsvFileHandler.readRecords()
            .map(parser::recordToUserDto)
    }

    override fun getAdmin(): UserDto = ADMIN

    override fun deleteUser(userId: UUID) {
        usersCsvFileHandler.readRecords()
            .map {
                val userDto = parser.recordToUserDto(it)
                if (userDto.id == userId)
                    parser.userDtoToRecord(userDto.copy(isDeleted = true))
                else it
            }
            .also(usersCsvFileHandler::rewriteRecords)
    }

    override fun addMate(userName: String, hashedPassword: String) {
        usersCsvFileHandler.appendRecord(
            UserDto(
                id = UUID.randomUUID(),
                userName = userName,
                hashedPassword = hashedPassword,
                type = User.Type.MATE.name,
                isDeleted = false
            ).let(parser::userDtoToRecord)
        )
    }

    override fun getLoggedInUser(): UserDto {
        return loggedInUser ?: throw NoLoggedInUserIsSavedInCacheException()
    }

    override fun setLoggedInUser(user: UserDto) {
        loggedInUserCsvFileHandler.rewriteRecords(
            listOf(parser.userDtoToRecord(user))
        )
        loggedInUser = user
    }

    override fun clearLoggedInUser() {
        loggedInUserCsvFileHandler.rewriteRecords(
            listOf()
        )
        loggedInUser = null
    }

    private fun loadUserFromLocalFile(): UserDto? {
        return loggedInUserCsvFileHandler.readRecords()
            .takeIf { it.isNotEmpty() }
            ?.let { parser.recordToUserDto(it[0]) }
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