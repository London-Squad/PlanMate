package data.dataSources.csvDataSource

import data.dataSources.csvDataSource.fileIO.CsvFileHandler
import data.dataSources.csvDataSource.fileIO.CsvParser
import data.repositories.dataSourceInterfaces.UsersDataSource
import data.dto.UserDto
import logic.entities.User
import logic.exceptions.UserNameAlreadyTakenException
import java.util.*

class CsvUsersDataSource(
    private val usersCsvFileHandler: CsvFileHandler,
    private val csvParser: CsvParser
) : UsersDataSource {

    override fun getMates(includeDeleted: Boolean): List<UserDto> {
        return usersCsvFileHandler.readRecords()
            .map(csvParser::recordToUserDto)
            .filter { if (includeDeleted) true else !it.isDeleted }
    }

    override fun getAdmin(): UserDto = ADMIN

    override fun deleteUser(userId: UUID) {
        usersCsvFileHandler.readRecords()
            .map {
                val userDto = csvParser.recordToUserDto(it)
                if (userDto.id == userId)
                    csvParser.userDtoToRecord(userDto.copy(isDeleted = true))
                else it
            }
            .also(usersCsvFileHandler::rewriteRecords)
    }

    override fun addMate(userName: String, hashedPassword: String) {
        usersCsvFileHandler.readRecords().forEach {
            val userDto = csvParser.recordToUserDto(it)
            if (userDto.userName == userName)
                throw UserNameAlreadyTakenException("User with username '$userName' already exists")
        }

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