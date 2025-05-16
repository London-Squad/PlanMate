package data.repositories

import data.repositories.dataSources.LoggedInUserCacheDataSource
import data.repositories.dataSources.UsersDataSource
import data.repositories.dtoMappers.user.UserDtoMapper
import data.repositories.dtoMappers.user.toUser
import data.security.hashing.HashingAlgorithm
import logic.entities.User
import logic.exceptions.UserNotFoundException
import logic.repositories.AuthenticationRepository

class AuthenticationRepositoryImpl<DTO>(
    private val usersDataSource: UsersDataSource<DTO>,
    private val userDtoMapper: UserDtoMapper<DTO>,
    private val loggedInUserCacheDataSource: LoggedInUserCacheDataSource,
    private val hashingAlgorithm: HashingAlgorithm
) : AuthenticationRepository {

    override suspend fun login(userName: String, password: String): User {
        val hashedPassword = hashingAlgorithm.hashData(password)

        val admin = usersDataSource.getAdmin(userName, hashedPassword)

        if (admin != null) return admin
            .run(userDtoMapper::mapToCachedUser)
            .also(loggedInUserCacheDataSource::setLoggedInUser)
            .toUser()

        return usersDataSource.getMate(userName, hashedPassword)
            ?.run(userDtoMapper::mapToCachedUser)
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