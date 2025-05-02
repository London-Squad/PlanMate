package data.cacheData

import logic.entities.User
import logic.exceptions.NoLoggedInUserIsSavedInCacheException
import logic.repositories.CacheDataRepository

class CacheDataSource : CacheDataRepository {

    private var loggedInUser: User? = null

    override fun getLoggedInUser(): User {
        if (loggedInUser == null) throw NoLoggedInUserIsSavedInCacheException()
        return loggedInUser!!
    }

    override fun setLoggedInUser(user: User) {
        loggedInUser = user
    }

    override fun clearLoggedInUserFromCache() {
        loggedInUser = null
    }
}