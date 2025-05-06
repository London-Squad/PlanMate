package data.mongoDBDataSource

import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import data.dataSources.UsersDataSource
import data.dto.UserDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.bson.Document
import java.util.UUID

class MongoDBUsersDataSource(
    private val collection: MongoCollection<Document> = DatabaseConnection.getUsersCollection()
) : UsersDataSource {

    companion object {
        private const val ID_FIELD = "id"
        private const val USERNAME_FIELD = "userName"
        private const val PASSWORD_FIELD = "hashedPassword"
        private const val TYPE_FIELD = "type"
        private const val IS_DELETED_FIELD = "isDeleted"
    }

    private var loggedInUserId: UUID? = null

    override fun getMates(): List<UserDto> {
        var result: List<UserDto> = emptyList()
        result = collection.find(Filters.eq(IS_DELETED_FIELD, false)).map { doc ->
            UserDto(
                id = UUID.fromString(doc.getString(ID_FIELD)),
                userName = doc.getString(USERNAME_FIELD),
                hashedPassword = doc.getString(PASSWORD_FIELD),
                type = doc.getString(TYPE_FIELD),
                isDeleted = doc.getBoolean(IS_DELETED_FIELD) ?: false
            )
        }.toList()
        return result
    }

    override fun getAdmin(): UserDto {
        lateinit var result: UserDto
        result = collection.find(Filters.eq(TYPE_FIELD, "ADMIN")).first().let { doc ->
            UserDto(
                id = UUID.fromString(doc.getString(ID_FIELD)),
                userName = doc.getString(USERNAME_FIELD),
                hashedPassword = doc.getString(PASSWORD_FIELD),
                type = doc.getString(TYPE_FIELD),
                isDeleted = doc.getBoolean(IS_DELETED_FIELD) ?: false
            )
        }
        return result
    }

    override fun deleteUser(userId: UUID) {
        CoroutineScope(Dispatchers.IO).launch {
            collection.updateOne(Filters.eq(ID_FIELD, userId.toString()), Updates.set(IS_DELETED_FIELD, true))
        }
    }

    override fun addMate(userName: String, hashedPassword: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val existingUser = collection.find(Filters.eq(USERNAME_FIELD, userName)).first()
            if (existingUser != null) throw IllegalStateException("User with username '$userName' already exists")

            val doc = Document(ID_FIELD, UUID.randomUUID().toString())
                .append(USERNAME_FIELD, userName)
                .append(PASSWORD_FIELD, hashedPassword)
                .append(TYPE_FIELD, "MATE")
                .append(IS_DELETED_FIELD, false)
            collection.insertOne(doc)
        }
    }

    override fun getLoggedInUser(): UserDto {
        val userId = loggedInUserId ?: throw IllegalStateException("No user is logged in")
        return collection.find(Filters.eq(ID_FIELD, userId.toString())).first()?.let { doc ->
            UserDto(
                id = UUID.fromString(doc.getString(ID_FIELD)),
                userName = doc.getString(USERNAME_FIELD),
                hashedPassword = doc.getString(PASSWORD_FIELD),
                type = doc.getString(TYPE_FIELD),
                isDeleted = doc.getBoolean(IS_DELETED_FIELD) ?: false
            )
        } ?: throw IllegalStateException("User with id '$userId' not found")
    }

    override fun setLoggedInUser(user: UserDto) {
        loggedInUserId = user.id
    }

    override fun clearLoggedInUser() {
        loggedInUserId = null
    }
}