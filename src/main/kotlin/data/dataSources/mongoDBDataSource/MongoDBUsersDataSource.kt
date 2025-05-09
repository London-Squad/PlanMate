package data.dataSources.mongoDBDataSource

import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import data.dataSources.mongoDBDataSource.mongoDBParse.MongoDBParse
import data.repositories.dataSourceInterfaces.UsersDataSource
import data.dto.UserDto
import data.exceptions.DataOperationException
import data.exceptions.handleException
import data.exceptions.sharedOperationTypes.MongoOperationName
import logic.planeMateException.NoLoggedInUserFoundException
import org.bson.Document
import java.util.UUID

class MongoDBUsersDataSource(
    private val collection: MongoCollection<Document>,
    private val mongoParser: MongoDBParse
) : UsersDataSource {

    private var loggedInUser: UserDto? = null

    override fun getMates(): List<UserDto> {
        return handleException(MongoOperationName.RETRIEVE_PROJECTS) {
            collection.find(Filters.eq(MongoDBParse.IS_DELETED_FIELD, false)).map { doc ->
                mongoParser.documentToUserDto(doc)
            }.toList()
        }
    }

    override fun getAdmin(): UserDto = ADMIN

    override fun deleteUser(userId: UUID) {
        handleException(MongoOperationName.DELETE_USER) {
            collection.updateOne(
                Filters.eq(MongoDBParse.ID_FIELD, userId.toString()),
                Updates.set(MongoDBParse.IS_DELETED_FIELD, true)
            )
        }
    }

    override fun addMate(userName: String, hashedPassword: String) {
        handleException(MongoOperationName.INSERT_USER) {
            val existingUser = collection.find(Filters.eq(MongoDBParse.USERNAME_FIELD, userName)).first()
            if (existingUser != null) throw DataOperationException("User with username '$userName' already exists")
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
        }
    }

    override fun getLoggedInUser(): UserDto {
        return loggedInUser ?: throw NoLoggedInUserFoundException()
    }

    override fun setLoggedInUser(user: UserDto) {
        loggedInUser = user
    }

    override fun clearLoggedInUser() {
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