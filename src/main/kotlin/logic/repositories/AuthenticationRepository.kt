package logic.repositories

import logic.entities.User
import java.util.*

interface AuthenticationRepository {

    fun getMates(includeDeleted: Boolean = false): List<User>

    fun getAdmin(): User

    fun deleteUser(userId: UUID)

    fun login(userName: String, password: String): User

    fun logout(): Boolean

    fun register(userName: String, password: String): Boolean

    fun getLoggedInUser(): User
}