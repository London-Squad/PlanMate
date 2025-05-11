package data.repositories

import data.repositories.dataSourceInterfaces.UsersDataSource
import data.repositories.dtoMappers.toUser
import data.security.hashing.HashingAlgorithm
import logic.entities.User
import logic.exceptions.UserNameAlreadyTakenException
import logic.repositories.UserRepository
import java.util.UUID

class UserRepositoryImpl(
    private val usersDataSource: UsersDataSource,
    private val hashingAlgorithm: HashingAlgorithm
) : UserRepository {

    override fun getMates(includeDeleted: Boolean): List<User> {
        return usersDataSource.getMates(includeDeleted)
            .map { it.toUser() }
    }

    override fun getAdmin(): User =
        usersDataSource.getAdmin().toUser()

    override fun deleteMate(userId: UUID) {
        usersDataSource.deleteUser(userId)
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
}
