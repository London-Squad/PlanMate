package data.repositoriesImpl

import data.dataSources.UsersDataSource
import data.csvDataSource.DtoMapper
import data.security.hashing.HashingAlgorithm
import logic.entities.User
import logic.exceptions.UserNotFoundException
import logic.exceptions.UserNameAlreadyTakenException
import logic.repositories.AuthenticationRepository
import java.util.*

class AuthenticationRepositoryImpl(
    private val usersDataSource: UsersDataSource,
    private val mapper: DtoMapper,
    private val hashingAlgorithm: HashingAlgorithm
) : AuthenticationRepository {
    override fun getMates(includeDeleted: Boolean): List<User> {
        return usersDataSource.getMates()
            .filter { if (includeDeleted) true else !it.isDeleted }
            .map(mapper::mapToUser)
    }

    override fun getAdmin(): User =
        usersDataSource.getAdmin()
            .let(mapper::mapToUser)

    override fun deleteUser(userId: UUID) {
        usersDataSource.deleteUser(userId)
    }

    override fun login(userName: String, password: String): User {
        val hashedPassword = hashingAlgorithm.hashData(password)

        val admin = usersDataSource.getAdmin()

        if (userName == admin.userName && hashedPassword == admin.hashedPassword)
            return admin
                .also(usersDataSource::setLoggedInUser)
                .let(mapper::mapToUser)

        return usersDataSource.getMates()
            .firstOrNull {
                it.userName == userName
                        && it.hashedPassword == hashedPassword
                        && !it.isDeleted
            }
            ?.also(usersDataSource::setLoggedInUser)
            ?.let(mapper::mapToUser)
            ?: throw UserNotFoundException()
    }

    override fun logout() {
        usersDataSource.clearLoggedInUser()
    }

    override fun addMate(userName: String, password: String) {
        getMates().any { user ->
            user.userName == userName
        }.let {
            if (it) throw UserNameAlreadyTakenException()
        }

        usersDataSource.addMate(
            userName,
            hashingAlgorithm.hashData(password)
        )
    }

    override fun getLoggedInUser(): User {
        return usersDataSource.getLoggedInUser()
            .let(mapper::mapToUser)
    }

}