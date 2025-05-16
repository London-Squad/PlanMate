package data.repositories

import data.repositories.dataSources.UsersDataSource
import data.repositories.dtoMappers.user.UserDtoMapper
import data.security.hashing.HashingAlgorithm
import logic.entities.User
import logic.exceptions.UserNameAlreadyExistsException
import logic.repositories.UserRepository
import java.util.*

class UserRepositoryImpl<DTO>(
    private val usersDataSource: UsersDataSource<DTO>,
    private val userDtoMapper: UserDtoMapper<DTO>,
    private val hashingAlgorithm: HashingAlgorithm
) : UserRepository {

    override suspend fun getUsers(includeDeleted: Boolean): List<User> {
        return usersDataSource.getUsers(includeDeleted)
            .map(userDtoMapper::mapToUser)
    }

    override suspend fun getMates(includeDeleted: Boolean): List<User> {
        return usersDataSource.getMates(includeDeleted)
            .map(userDtoMapper::mapToUser)
    }

    override suspend fun deleteMate(userId: UUID) {
        usersDataSource.deleteUser(userId)
    }

    override suspend fun addMate(userName: String, password: String) {
        getMates().any { user ->
            user.userName == userName
        }.let {
            if (it) throw UserNameAlreadyExistsException()
        }

        usersDataSource.addMate(
            userName,
            hashingAlgorithm.hashData(password)
        )
    }
}
