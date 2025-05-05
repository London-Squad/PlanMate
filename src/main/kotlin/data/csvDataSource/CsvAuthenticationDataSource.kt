package data.csvDataSource

import data.csvDataSource.fileIO.CsvFileHandler
import data.csvDataSource.fileIO.Parser
import data.dataSources.UsersDataSource
import data.entitiesData.UserData
import logic.entities.User
import logic.exceptions.NoLoggedInUserIsSavedInCacheException
import java.util.*

class CsvAuthenticationDataSource(
    private val usersCsvFileHandler: CsvFileHandler,
    private val loggedInUserCsvFileHandler: CsvFileHandler,
    private val parser: Parser
) : UsersDataSource {
    private var loggedInUser: UserData? = null

    init {
        loggedInUser = loadUserFromLocalFile()
    }

    override fun getMates(): List<UserData> {
        return usersCsvFileHandler.readRecords()
            .map(parser::recordToUserData)
    }

    override fun getAdmin(): UserData = ADMIN

    override fun deleteUser(userId: UUID) {
        usersCsvFileHandler.readRecords()
            .map {
                val userData = parser.recordToUserData(it)
                if (userData.id == userId)
                    parser.userDataToRecord(userData.copy(isDeleted = true))
                else it
            }
            .also(usersCsvFileHandler::rewriteRecords)
    }

    override fun register(userName: String, hashedPassword: String) {
        usersCsvFileHandler.appendRecord(
            UserData(
                id = UUID.randomUUID(),
                userName = userName,
                hashedPassword = hashedPassword,
                type = User.Type.MATE.name,
                isDeleted = false
            ).let(parser::userDataToRecord)
        )
    }

    override fun getLoggedInUser(): UserData {
        return loggedInUser ?: throw NoLoggedInUserIsSavedInCacheException()
    }

    override fun setLoggedInUser(user: UserData) {
        loggedInUserCsvFileHandler.rewriteRecords(
            listOf(parser.userDataToRecord(user))
        )
        loggedInUser = user
    }

    override fun clearLoggedInUser() {
        loggedInUserCsvFileHandler.rewriteRecords(
            listOf()
        )
        loggedInUser = null
    }

    private fun loadUserFromLocalFile(): UserData? {
        return loggedInUserCsvFileHandler.readRecords()
            .takeIf { it.isNotEmpty() }
            ?.let { parser.recordToUserData(it[0]) }
    }

    companion object {
        private val ADMIN = UserData(
            id = UUID.fromString("5750f82c-c1b6-454d-b160-5b14857bc9dc"),
            userName = "admin",
            hashedPassword = "2e6e5a2b38ba905790605c9b101497bc",
            type = "ADMIN",
            isDeleted = false
        )
    }
}