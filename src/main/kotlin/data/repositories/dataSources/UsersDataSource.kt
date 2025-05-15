package data.repositories.dataSources

import data.dto.UserDto
import java.util.*

interface UsersDataSource {
    suspend fun getMates(includeDeleted: Boolean): List<UserDto>
    suspend fun getAdmin(): UserDto
    suspend fun deleteUser(userId: UUID)
    suspend fun addMate(userName: String, hashedPassword: String)
}