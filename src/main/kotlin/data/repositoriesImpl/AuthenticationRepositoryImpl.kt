package data.repositoriesImpl

import data.csvDataSource.CsvAuthenticationDataSource
import logic.entities.User
import logic.repositories.AuthenticationRepository
import java.util.*

class AuthenticationRepositoryImpl(
    private val authenticationDataSource: CsvAuthenticationDataSource
) : AuthenticationRepository {
    override fun getMates(): List<User> {
        TODO("Not yet implemented")
//        return authenticationDataSource.getMates().map{ dbUser ->
//            User(dbUser.id, dbUser.userName, getUserType(dbUser.type))
//        }
    }

    private fun getUserType(userType: String): User.Type {
        if (userType == "admin") return User.Type.ADMIN
        return User.Type.MATE
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

}