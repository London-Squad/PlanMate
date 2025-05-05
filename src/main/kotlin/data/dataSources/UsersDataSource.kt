package data.dataSources

import data.entitiesData.UserData
import java.util.UUID

interface UsersDataSource {
    fun getMates(): List<UserData>
    fun getAdmin(): UserData
    fun deleteUser(userId: UUID)
    fun register(userName: String, hashedPassword: String)
    fun getLoggedInUser(): UserData
    fun setLoggedInUser(user: UserData)
    fun clearLoggedInUser()
}