package data.repositories.dataSources

import data.dto.UserCsvDto

interface LoggedInUserCacheDataSource {
    fun getLoggedInUser(): UserCsvDto
    fun setLoggedInUser(user: UserCsvDto)
    fun clearLoggedInUser()
}