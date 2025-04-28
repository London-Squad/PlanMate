package data.repository

import logic.entities.User
import logic.repositories.AuthenticationRepository
import java.util.*

class AuthenticationRepositoryImpl: AuthenticationRepository {
    override fun login(userName: String, password: String): User {
        TODO("Not yet implemented")
    }

    override fun register(userName: String, password: String): User {
        TODO("Not yet implemented")
    }

    override fun changePassword(userId: UUID, currentPassword: String, newPassword: String) {
        TODO("Not yet implemented")
    }
}