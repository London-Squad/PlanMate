package data.dataSources

import data.dto.UserDto
import java.util.UUID

interface UsersDataSource {
    fun getMates(): List<UserDto>
    fun getAdmin(): UserDto
    fun deleteUser(userId: UUID)
    fun addMate(userName: String, hashedPassword: String)
    fun getLoggedInUser(): UserDto
    fun setLoggedInUser(user: UserDto)
    fun clearLoggedInUser()
}