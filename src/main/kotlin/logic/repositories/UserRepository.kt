package logic.repositories

import logic.entities.User
import java.util.*

interface UserRepository {
    suspend fun getMates(includeDeleted: Boolean = false): List<User>
    suspend fun getUserNameById(userId: UUID):String
    suspend fun getAdmin(): User
    suspend fun deleteMate(userId: UUID)
    suspend fun addMate(userName: String, password: String)
}
