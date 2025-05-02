package logic.repositories

import logic.entities.User

interface CacheDataRepository {

    fun getLoggedInUser(): User

    fun setLoggedInUser(user: User)

    fun clearLoggedInUserFromCache()

}