package logic.repositories

import logic.entities.User
import java.util.UUID

interface AuthenticationRepository {

    fun login(userName: String, password: String): User

    fun register(userName: String, password: String): User

    fun changePassword(userId: UUID, currentPassword: String, newPassword: String)

}