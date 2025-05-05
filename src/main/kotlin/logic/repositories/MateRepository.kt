package logic.repositories

import logic.entities.User
import java.util.*

interface MateRepository {
    fun getMates(): List<User>

    fun deleteUser(userId: UUID)

    fun addMate(userName: String, password: String): Boolean

    fun changeMatePassword(userName: String, currentPassword: String, newPassword: String): Boolean
}