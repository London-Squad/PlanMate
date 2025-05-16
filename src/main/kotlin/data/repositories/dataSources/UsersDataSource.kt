package data.repositories.dataSources

import java.util.*

interface UsersDataSource<DTO> {
    suspend fun getUsers(includeDeleted: Boolean): List<DTO>
    suspend fun getMates(includeDeleted: Boolean): List<DTO>
    suspend fun deleteUser(userId: UUID)
    suspend fun addMate(userName: String, hashedPassword: String)
    suspend fun getMate(userName: String, hashedPassword: String): DTO?
    suspend fun getAdmin(userName: String, hashedPassword: String): DTO?
}