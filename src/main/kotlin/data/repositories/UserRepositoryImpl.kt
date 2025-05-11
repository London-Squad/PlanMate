package data.repositories

import data.repositories.dataSourceInterfaces.UsersDataSource
import data.repositories.dtoMappers.toUser
import data.security.hashing.HashingAlgorithm
import logic.entities.User
import logic.exceptions.UserNameAlreadyExistException
import logic.repositories.UserRepository
import java.util.UUID

class UserRepositoryImpl(
    private val usersDataSource: UsersDataSource,
    private val hashingAlgorithm: HashingAlgorithm
) : UserRepository {

    override suspend fun getMates(includeDeleted: Boolean): List<User> {
        return usersDataSource.getMates()
            .filter { if (includeDeleted) true else !it.isDeleted }
            .map { it.toUser() }
    }

    override suspend fun getAdmin(): User =
        usersDataSource.getAdmin().toUser()

    override suspend fun deleteMate(userId: UUID) {
        usersDataSource.deleteUser(userId)
    }

    override suspend fun addMate(userName: String, password: String) {
        getMates().any { user ->
            user.userName == userName
        }.let {
            if (it) throw UserNameAlreadyExistException()
        }

        usersDataSource.addMate(
            userName,
            hashingAlgorithm.hashData(password)
        )
    }
}
