package logic.repositories

import logic.entities.User
import java.util.UUID

interface UserRepository {
    suspend fun getMates(includeDeleted: Boolean = false): List<User>
    suspend fun getAdmin(): User
    suspend fun deleteMate(userId: UUID)
    suspend fun addMate(userName: String, password: String)
    suspend fun getUsers():List<User>
    suspend fun getUserById(userId: UUID): User
}
