package data.dataSources.mongoDBDataSource

import com.mongodb.MongoException
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoCollection
import data.dto.UserMongoDto
import data.repositories.dataSources.UsersDataSource
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import logic.exceptions.RetrievingDataFailureException
import logic.exceptions.StoringDataFailureException
import logic.exceptions.UserNameAlreadyExistsException
import logic.exceptions.UserNotFoundException
import java.util.*

class MongoDBUsersDataSource(
    private val collection: MongoCollection<UserMongoDto>
) : UsersDataSource<UserMongoDto> {

    override suspend fun getUsers(includeDeleted: Boolean): List<UserMongoDto> {
        return collection.find<UserMongoDto>().toList()
    }

    override suspend fun getMates(includeDeleted: Boolean): List<UserMongoDto> {
        return try {
            val filter = if (!includeDeleted) Filters.eq(UserMongoDto::isDeleted.name, false)
            else Filters.empty()

            collection.find(filter).toList()
        } catch (e: MongoException) {
            throw RetrievingDataFailureException("Failed to retrieve users: ${e.message}")
        }
    }

    override suspend fun getAdmin(userName: String, hashedPassword: String): UserMongoDto? {
        return if (userName == ADMIN.userName && hashedPassword == ADMIN.hashedPassword) ADMIN
        else null
    }

    override suspend fun deleteUser(userId: UUID) {
        val filters = Filters.and(
            Filters.eq("_id", userId.toString()),
            Filters.eq(UserMongoDto::isDeleted.name, false)
        )
        try {
            collection.updateOne(filters, Updates.set(UserMongoDto::isDeleted.name, true))
                .apply { if (matchedCount == 0L) throw UserNotFoundException() }

        } catch (e: MongoException) {
            throw StoringDataFailureException("Failed to delete user: ${e.message}")
        }
    }

    override suspend fun addMate(userName: String, hashedPassword: String) {
        try {
            val existingUser = collection.find(Filters.eq(UserMongoDto::userName.name, userName)).firstOrNull()
            if (existingUser != null) throw UserNameAlreadyExistsException("User with username '$userName' already exists")

            collection.insertOne(
                UserMongoDto(
                    UUID.randomUUID().toString(), userName, hashedPassword, "MATE", isDeleted = false
                )
            )
        } catch (e: MongoException) {
            throw StoringDataFailureException("Failed to add mate: ${e.message}")
        }
    }

    override suspend fun getMate(userName: String, hashedPassword: String): UserMongoDto? {
        val filters = Filters.and(
            listOf(
                Filters.eq(UserMongoDto::userName.name, userName),
                Filters.eq(UserMongoDto::hashedPassword.name, hashedPassword)
            )
        )
        return collection.find(filters).firstOrNull()
    }

    companion object {
        private val ADMIN = UserMongoDto(
            id = "5750f82c-c1b6-454d-b160-5b14857bc9dc",
            userName = "admin",
            hashedPassword = "2e6e5a2b38ba905790605c9b101497bc",
            type = "ADMIN",
            isDeleted = false
        )
    }
}