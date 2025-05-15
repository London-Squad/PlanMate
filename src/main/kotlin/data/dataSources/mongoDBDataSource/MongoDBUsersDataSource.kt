package data.dataSources.mongoDBDataSource

import com.mongodb.client.model.Filters
import data.dataSources.mongoDBDataSource.mongoDBHandler.MongoDBParser
import data.dataSources.mongoDBDataSource.mongoDBHandler.MongoDBQueryHandler
import data.dto.UserDto
import data.repositories.dataSources.UsersDataSource
import logic.exceptions.NotFoundException
import logic.exceptions.UserNameAlreadyExistsException
import java.util.*

class MongoDBUsersDataSource(
    private val usersQueryHandler: MongoDBQueryHandler, private val mongoParser: MongoDBParser
) : UsersDataSource {

    override suspend fun getMates(includeDeleted: Boolean): List<UserDto> {
        val filters = if (!includeDeleted) Filters.eq(MongoDBParser.IS_DELETED_FIELD, false)
        else Filters.empty()

        return usersQueryHandler.fetchManyFromCollection(filters).map(mongoParser::documentToUserDto)
    }

    override suspend fun getAdmin(): UserDto = ADMIN

    override suspend fun deleteUser(userId: UUID) {
        val filters = Filters.and(
            Filters.eq(MongoDBParser.ID_FIELD, userId.toString()), Filters.eq(MongoDBParser.IS_DELETED_FIELD, false)
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
                UUID.randomUUID().toString(), userName, hashedPassword, "MATE", isDeleted = false
            )
        )
        usersQueryHandler.insertToCollection(doc)
    }

    override suspend fun getUserById(
        userId: UUID,
        includeDeleted: Boolean
    ): UserDto {
        val filters = if (includeDeleted) {
            Filters.eq(MongoDBParser.ID_FIELD, userId.toString())
        } else {
            Filters.and(
                Filters.eq(MongoDBParser.ID_FIELD, userId.toString()),
                Filters.eq(MongoDBParser.IS_DELETED_FIELD, false)
            )
        }

        val document = usersQueryHandler.fetchOneFromCollection(filters)
        return mongoParser.documentToUserDto(document)
    }

    override suspend fun getUserNameById(userId: UUID): String {
        return getUserById(userId,false).userName
    }


    companion object {
        private val ADMIN = UserDto(
            id = "5750f82c-c1b6-454d-b160-5b14857bc9dc",
            userName = "admin",
            hashedPassword = "2e6e5a2b38ba905790605c9b101497bc",
            type = "ADMIN",
            isDeleted = false
        )
    }
}