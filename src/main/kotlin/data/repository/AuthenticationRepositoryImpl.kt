package data.repository

import logic.AuthenticationRepository
import logic.entity.User

class AuthenticationRepositoryImpl: AuthenticationRepository {
    override fun login(userName: String, password: String): User {
        TODO("Not yet implemented")
    }

    override fun createMateAccount(userName: String, password: String): User {
        TODO("Not yet implemented")
    }
}