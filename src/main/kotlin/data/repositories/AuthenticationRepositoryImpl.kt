package data.repositories

import data.repositories.dataSourceInterfaces.UsersDataSource
import data.repositories.dtoMappers.toUser
import data.security.hashing.HashingAlgorithm
import logic.entities.User
import logic.exceptions.UserNotFoundException
import logic.repositories.AuthenticationRepository

class AuthenticationRepositoryImpl(
    private val usersDataSource: UsersDataSource,
    private val hashingAlgorithm: HashingAlgorithm
) : AuthenticationRepository {

    override suspend fun login(userName: String, password: String): User {
        val hashedPassword = hashingAlgorithm.hashData(password)

        val admin = usersDataSource.getAdmin()

        if (userName == admin.userName && hashedPassword == admin.hashedPassword)
            return admin
            .also { usersDataSource.setLoggedInUser(it) }.toUser()

        return usersDataSource.getMates()
                .firstOrNull {
                    it.userName == userName
                            && it.hashedPassword == hashedPassword
                            && !it.isDeleted
                }
                ?.also { usersDataSource.setLoggedInUser(it) }?.toUser()
            ?: throw UserNotFoundException()
    }

    override suspend fun logout() {
        usersDataSource.clearLoggedInUser()
    }

    override suspend fun getLoggedInUser(): User {
        return usersDataSource.getLoggedInUser().toUser()
    }
}