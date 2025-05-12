package data.repositories.dataSources

import data.dto.UserDto
import java.util.UUID

interface UsersDataSource {
    fun getMates(includeDeleted: Boolean): List<UserDto>
    fun getAdmin(): UserDto
    fun deleteUser(userId: UUID)
    fun addMate(userName: String, hashedPassword: String)
}