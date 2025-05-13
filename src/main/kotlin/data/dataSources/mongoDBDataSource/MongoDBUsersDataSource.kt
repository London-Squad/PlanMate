package data.dataSources.mongoDBDataSource

import com.mongodb.client.model.Filters
import data.dataSources.mongoDBDataSource.mongoDBParser.MongoDBParser
import data.dataSources.mongoDBDataSource.mongoDBParser.MongoDBQueryHandler
import data.dto.UserDto
import data.repositories.dataSources.UsersDataSource
import logic.exceptions.*
import java.util.*

class MongoDBUsersDataSource(
    private val usersQueryHandler: MongoDBQueryHandler,
    private val mongoParser: MongoDBParser
) : UsersDataSource {

    override suspend fun getMates(includeDeleted: Boolean): List<UserDto> {
        val filters = if (!includeDeleted) Filters.eq(MongoDBParser.IS_DELETED_FIELD, false)
        else Filters.empty()

        return usersQueryHandler.fetchManyFromCollection(filters).map(mongoParser::documentToUserDto)
    }

    override suspend fun getAdmin(): UserDto = ADMIN

    override suspend fun deleteUser(userId: UUID) {
        val filters = Filters.and(
            Filters.eq(MongoDBParser.ID_FIELD, userId.toString()),
            Filters.eq(MongoDBParser.IS_DELETED_FIELD, false)
        )
        usersQueryHandler.softDeleteFromCollection(filters)
    }

    override suspend fun addMate(userName: String, hashedPassword: String) {
        try {
            usersQueryHandler.fetchOneFromCollection(Filters.eq(MongoDBParser.USERNAME_FIELD, userName))
        } catch (e: NotFoundException) {
            throw UserNameAlreadyExistsException("User with username '$userName' already exists")
        }

        val doc = mongoParser.userDtoToDocument(
            UserDto(
                UUID.randomUUID(), userName, hashedPassword, "MATE", isDeleted = false
            )
        )
        usersQueryHandler.insertToCollection(doc)
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