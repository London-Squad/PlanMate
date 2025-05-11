package data.repositories.dataSourceInterfaces

import data.dto.UserDto

interface LoggedInUserCacheDataSource {
    fun getLoggedInUser(): UserDto
    fun setLoggedInUser(user: UserDto)
    fun clearLoggedInUser()
}