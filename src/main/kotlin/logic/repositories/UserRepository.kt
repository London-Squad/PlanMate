package logic.repositories

import logic.entities.User
import java.util.UUID

interface UserRepository {
    fun getMates(includeDeleted: Boolean = false): List<User>
    fun getAdmin(): User
    fun deleteUser(userId: UUID)
    fun addMate(userName: String, password: String)
}
