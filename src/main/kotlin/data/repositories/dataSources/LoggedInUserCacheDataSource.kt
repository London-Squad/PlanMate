package data.repositories.dataSources

import data.dto.UserDto

interface LoggedInUserCacheDataSource {
    fun getLoggedInUser(): UserDto
    fun setLoggedInUser(user: UserDto)
    fun clearLoggedInUser()
}