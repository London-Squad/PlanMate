package data.mongoDBDataSource

import logic.entities.User
import logic.repositories.AuthenticationRepository
import java.util.*

class MongoDBAuthenticationDataSource() : AuthenticationRepository {
    override fun getMates(): List<User> {
        TODO("Not yet implemented")
    }

    override fun deleteUser(userId: UUID) {
        TODO("Not yet implemented")
    }

    override fun login(userName: String, password: String): User {
        TODO("Not yet implemented")
    }

    override fun logout(): Boolean {
        TODO("Not yet implemented")
    }

    override fun register(userName: String, password: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun changePassword(userName: String, currentPassword: String, newPassword: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun getLoggedInUser(): User {
        TODO("Not yet implemented")
    }

    private companion object {
        const val ID_FIELD = "id"
        const val USERNAME_FIELD = "userName"
        const val PASSWORD_FIELD = "hashedPassword"
        const val TYPE_FIELD = "type"
        const val IS_DELETED_FIELD_FIELD = "isDeleted"
    }
}