package data.cacheData

import logic.entities.User
import logic.repositories.CacheDataRepository

class CacheDataRepositoryImpl : CacheDataRepository {

    private var loggedInUser: User? = User(userName = "admin", type = User.Type.ADMIN)

    override fun getLoggedInUser(): User? = loggedInUser

    override fun setLoggedInUser(user: User) {
        loggedInUser = user
    }

    override fun clearLoggedInUserFromCatch() {
        loggedInUser = null
    }
}