package data.dataSources.mongoDBDataSource

import com.mongodb.MongoException
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoCollection
import data.dataSources.mongoDBDataSource.mongoDBParse.MongoDBParse
import data.repositories.dataSourceInterfaces.UsersDataSource
import data.dto.UserDto
import logic.exceptions.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import logic.exceptions.NoLoggedInUserFoundException
import org.bson.Document
import java.util.UUID
import kotlinx.coroutines.flow.toList

class MongoDBUsersDataSource(
    private val collection: MongoCollection<Document>,
    private val mongoParser: MongoDBParse
) : UsersDataSource {

    private var loggedInUser: UserDto? = null

        override suspend fun getMates(): List<UserDto> {
        try {
            return collection.find(Filters.eq(MongoDBParse.IS_DELETED_FIELD, false)).map { doc ->
                mongoParser.documentToUserDto(doc)
            }.toList()
        }catch (e: MongoException) {
            throw RetrievingDataFailureException("Failed to retrieve users: ${e.message}")
        }
    }

    override suspend fun getAdmin(): UserDto = ADMIN

    override suspend fun deleteUser(userId: UUID) {
        try {
            val result = collection.updateOne(
                Filters.and(
                    Filters.eq(MongoDBParse.ID_FIELD, userId.toString()),
                    Filters.eq(MongoDBParse.IS_DELETED_FIELD, false)
                ), Updates.set(MongoDBParse.IS_DELETED_FIELD, true)
            )
            if (result.matchedCount.toInt() == 0) {
                throw UserNotFoundException("User with ID $userId not found")
            }
        }catch (e: MongoException) {
            throw StoringDataFailureException("Failed to delete user: ${e.message}")
        }
    }

    override suspend fun addMate(userName: String, hashedPassword: String) {
        val existingUser = collection.find(Filters.eq(MongoDBParse.USERNAME_FIELD, userName)).first()
        if (existingUser != null) throw UserNameAlreadyExistException("User with username '$userName' already exists")
        try{
        val doc = mongoParser.userDtoToDocument(
            UserDto(
                UUID.randomUUID(),
                userName,
                hashedPassword,
                "MATE",
                isDeleted = false
            )
        )
        collection.insertOne(doc)
    }catch (e: MongoException) {
            throw StoringDataFailureException("Failed to add mate: ${e.message}")
        }

    }

    override suspend fun getLoggedInUser(): UserDto {
        return loggedInUser ?: throw NoLoggedInUserFoundException()
    }

    override suspend fun setLoggedInUser(user: UserDto) {
        loggedInUser = user
    }

    override suspend fun clearLoggedInUser() {
        loggedInUser = null
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