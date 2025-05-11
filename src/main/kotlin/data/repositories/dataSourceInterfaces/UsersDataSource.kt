package data.repositories.dataSourceInterfaces

import data.dto.UserDto
import java.util.*

interface UsersDataSource {
    suspend fun getMates(): List<UserDto>
    suspend fun getAdmin(): UserDto
    suspend fun deleteUser(userId: UUID)
    suspend fun addMate(userName: String, hashedPassword: String)
    suspend fun getLoggedInUser(): UserDto
    suspend fun setLoggedInUser(user: UserDto)
    suspend fun clearLoggedInUser()
}