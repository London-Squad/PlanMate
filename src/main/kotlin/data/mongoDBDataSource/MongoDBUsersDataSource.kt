package data.mongoDBDataSource

import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import data.dataSources.UsersDataSource
import data.dto.UserDto
import logic.exceptions.NoLoggedInUserFoundException
import logic.exceptions.UserNameAlreadyTakenException
import org.bson.Document
import java.util.UUID

class MongoDBUsersDataSource(
    private val collection: MongoCollection<Document> = DatabaseConnection.getUsersCollection()
) : UsersDataSource {

    private var loggedInUser: UserDto? = null

    override fun getMates(): List<UserDto> {
        return collection.find(Filters.eq(IS_DELETED_FIELD, false)).map { doc ->
            UserDto(
                id = UUID.fromString(doc.getString(ID_FIELD)),
                userName = doc.getString(USERNAME_FIELD),
                hashedPassword = doc.getString(PASSWORD_FIELD),
                type = doc.getString(TYPE_FIELD),
                isDeleted = doc.getBoolean(IS_DELETED_FIELD) ?: false
            )
        }.toList()
    }

    override fun getAdmin(): UserDto = ADMIN

    override fun deleteUser(userId: UUID) {
            collection.updateOne(Filters.eq(ID_FIELD, userId.toString()), Updates.set(IS_DELETED_FIELD, true))

    }

    override fun addMate(userName: String, hashedPassword: String) {
            val existingUser = collection.find(Filters.eq(USERNAME_FIELD, userName)).first()
        if (existingUser != null) throw UserNameAlreadyTakenException()
            val doc = Document(ID_FIELD, UUID.randomUUID().toString())
                .append(USERNAME_FIELD, userName)
                .append(PASSWORD_FIELD, hashedPassword)
                .append(TYPE_FIELD, "MATE")
                .append(IS_DELETED_FIELD, false)
            collection.insertOne(doc)
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
        private const val ID_FIELD = "id"
        private const val USERNAME_FIELD = "userName"
        private const val PASSWORD_FIELD = "hashedPassword"
        private const val TYPE_FIELD = "type"
        private const val IS_DELETED_FIELD = "isDeleted"
        private val ADMIN = UserDto(
            id = UUID.fromString("5750f82c-c1b6-454d-b160-5b14857bc9dc"),
            userName = "admin",
            hashedPassword = "2e6e5a2b38ba905790605c9b101497bc",
            type = "ADMIN",
            isDeleted = false
        )
    }
}