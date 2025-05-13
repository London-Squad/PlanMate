package data.dataSources.mongoDBDataSource

import com.mongodb.MongoException
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoCollection
import data.dataSources.mongoDBDataSource.mongoDBParse.MongoDBParse
import data.dto.UserDto
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import data.repositories.dataSources.UsersDataSource
import kotlinx.coroutines.flow.firstOrNull
import logic.exceptions.RetrievingDataFailureException
import logic.exceptions.StoringDataFailureException
import logic.exceptions.UserNameAlreadyExistsException
import logic.exceptions.UserNotFoundException
import org.bson.Document
import java.util.*

class MongoDBUsersDataSource(
    private val collection: MongoCollection<Document>, private val mongoParser: MongoDBParse
) : UsersDataSource {

    override suspend fun getMates(includeDeleted: Boolean): List<UserDto> {
        return try {
            val filter = if (!includeDeleted) Filters.eq(MongoDBParse.IS_DELETED_FIELD, false)
            else Filters.empty()

            collection.find(filter).map(mongoParser::documentToUserDto)
                .toList()
        } catch (e: MongoException) {
            throw RetrievingDataFailureException("Failed to retrieve users: ${e.message}")
        }
    }

    override suspend fun getAdmin(): UserDto = ADMIN

    override suspend fun deleteUser(userId: UUID) {
        val filters = Filters.and(
            Filters.eq(MongoDBParse.ID_FIELD, userId.toString()),
            Filters.eq(MongoDBParse.IS_DELETED_FIELD, false)
        )
        try {
            collection.updateOne(filters, Updates.set(MongoDBParse.IS_DELETED_FIELD, true))
                .apply { if (matchedCount == 0L) throw UserNotFoundException() }

        } catch (e: MongoException) {
            throw StoringDataFailureException("Failed to delete user: ${e.message}")
        }
    }

    override suspend fun addMate(userName: String, hashedPassword: String) {
        try {
            val existingUser = collection.find(Filters.eq(MongoDBParse.USERNAME_FIELD, userName)).firstOrNull()
            if (existingUser != null) throw UserNameAlreadyExistsException("User with username '$userName' already exists")

            val doc = mongoParser.userDtoToDocument(
                UserDto(
                    UUID.randomUUID(), userName, hashedPassword, "MATE", isDeleted = false
                )
            )
            collection.insertOne(doc)
        } catch (e: MongoException) {
            throw StoringDataFailureException("Failed to add mate: ${e.message}")
        }
    }

    override fun getUserById(userId: UUID): UserDto {
        try {
            val doc = collection.find(
                Filters.and(
                    Filters.eq(MongoDBParse.ID_FIELD, userId.toString()),
                    Filters.eq(MongoDBParse.IS_DELETED_FIELD, false)
                )
            ).first()

            return doc?.let { mongoParser.documentToUserDto(it) }
                ?: throw UserNotFoundException("User with ID $userId not found")
        } catch (e: MongoException) {
            throw RetrievingDataFailureException("Failed to retrieve user by ID: ${e.message}")
        }
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