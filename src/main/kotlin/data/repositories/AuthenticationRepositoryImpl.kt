package data.repositories

import data.repositories.dataSources.LoggedInUserCacheDataSource
import data.repositories.dataSources.UsersDataSource
import data.repositories.dtoMappers.toUser
import data.security.hashing.HashingAlgorithm
import logic.entities.User
import logic.exceptions.UserNotFoundException
import logic.repositories.AuthenticationRepository

class AuthenticationRepositoryImpl(
    private val usersDataSource: UsersDataSource,
    private val loggedInUserCacheDataSource: LoggedInUserCacheDataSource,
    private val hashingAlgorithm: HashingAlgorithm
) : AuthenticationRepository {

    override suspend fun login(userName: String, password: String): User {
        val hashedPassword = hashingAlgorithm.hashData(password)

        val admin = usersDataSource.getAdmin()

        if (userName == admin.userName && hashedPassword == admin.hashedPassword)
            return admin
                .also(loggedInUserCacheDataSource::setLoggedInUser)
                .toUser()

        return usersDataSource.getMates(includeDeleted = false)
            .firstOrNull { it.userName == userName && it.hashedPassword == hashedPassword }
            ?.also(loggedInUserCacheDataSource::setLoggedInUser)
            ?.toUser() ?: throw UserNotFoundException()
    }

    override suspend fun logout() {
        loggedInUserCacheDataSource.clearLoggedInUser()
    }

    override fun getLoggedInUser(): User {
        return loggedInUserCacheDataSource.getLoggedInUser().toUser()
    }
}