package logic.repositories

import logic.entities.User
import java.util.*

interface AuthenticationRepository {

    fun getMates(): List<User>

    fun deleteUser(userId: UUID)

    fun login(userName: String, password: String): User

    fun logout(): Boolean

    fun register(userName: String, password: String): Boolean

    fun getLoggedInUser(): User
}