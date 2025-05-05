package data

import data.fileIO.UserFileHelper
import data.fileIO.createFileIfNotExist
import data.security.hashing.HashingAlgorithm
import logic.entities.User
import logic.exceptions.UserNotFoundException
import logic.repositories.AuthenticationRepository
import logic.repositories.CacheDataRepository
import logic.validation.CredentialValidator
import java.io.File
import java.util.*

class CsvAuthenticationDataSource(
    private val userFile: File,
    private val hashingAlgorithm: HashingAlgorithm,
    private val credentialValidator: CredentialValidator,
    private val cacheDataRepository: CacheDataRepository
) : AuthenticationRepository {

    init {
        userFile.createFileIfNotExist("id,userName,password,type\n")
    }

    override fun login(userName: String, password: String): User {
        validateLoginInputs(userName, password)
        val user: User = when (isAdminUser(userName, password)) {
            true -> ADMIN
            false -> getMateOrThrowException(userName, password)
        }
        cacheDataRepository.setLoggedInUser(user)
        return user
    }

    override fun logout(): Boolean {
        cacheDataRepository.clearLoggedInUserFromCache()
        return true
    }

    private fun isAdminUser(userName: String, password: String): Boolean {
        return userName == ADMIN.userName && password == ADMIN_PASSWORD
    }

    private fun validateLoginInputs(userName: String, password: String) {
        credentialValidator.takeIfValidNameOrThrowException(userName)
        credentialValidator.takeIfValidPasswordOrThrowException(password)
    }

    private fun getMateOrThrowException(userName: String, password: String): User {
        val hashedPassword = hashingAlgorithm.hashData(password)
        return UserFileHelper.readUserOrNull(userFile, userName, hashedPassword) ?: throw UserNotFoundException()
    }

    companion object {
        val ADMIN = User(
            id = UUID.fromString("5750f82c-c1b6-454d-b160-5b14857bc9dc"),
            userName = "admin",
            type = User.Type.ADMIN
        )
        private const val ADMIN_PASSWORD = "Admin12"
    }
}