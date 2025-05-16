package logic.repositories

import logic.entities.User
import java.util.*

interface UserRepository {
    suspend fun getUsers(includeDeleted: Boolean): List<User>
    suspend fun getMates(includeDeleted: Boolean = false): List<User>
    suspend fun deleteMate(userId: UUID)
    suspend fun addMate(userName: String, password: String)
}
