package logic.repositories

import logic.entities.User

interface AuthenticationRepository {
    suspend fun login(userName: String, password: String): User
    suspend fun logout()
    fun getLoggedInUser(): User
}