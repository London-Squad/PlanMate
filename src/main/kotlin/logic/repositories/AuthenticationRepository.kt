package logic.repositories

import logic.entities.User
import java.util.UUID

interface AuthenticationRepository {

    fun getMates(): List<User>

    fun deleteUser(userId: UUID)

    fun login(userName: String, password: String): User

    fun logout()

    fun register(userName: String, password: String): User

    fun changePassword(userId: UUID, currentPassword: String, newPassword: String)

}