package logic.repositories

import logic.entities.User

interface AuthenticationRepository {

    fun login(userName: String, password: String): User

    fun logout(): Boolean
}