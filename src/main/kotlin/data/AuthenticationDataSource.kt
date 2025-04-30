package data

import logic.entities.User
import logic.repositories.AuthenticationRepository
import java.util.*

class AuthenticationDataSource : AuthenticationRepository {
    override fun getMates(): List<User> {
//        TODO("Not yet implemented")
        return listOf()
    }

    override fun deleteUser(userId: UUID) {
//        TODO("Not yet implemented")
    }

    override fun login(userName: String, password: String): User {
        if (userName == ADMIN.userName && password == ADMIN_PASSWORD) {
            return ADMIN
        }
        return User(userName = userName, type = User.Type.MATE)
    }

    override fun logout() {
//        TODO("Not yet implemented")
    }

    override fun register(userName: String, password: String): User {
//        TODO("Not yet implemented")
        return User(userName = userName, type = User.Type.MATE)
    }

    override fun changePassword(userId: UUID, currentPassword: String, newPassword: String) {
//        TODO("Not yet implemented")
    }

    private companion object {
        val ADMIN = User(
            id = UUID.fromString("5750f82c-c1b6-454d-b160-5b14857bc9dc"),
            userName = "admin",
            type = User.Type.ADMIN
        )
        const val ADMIN_PASSWORD = "admin"
    }
}