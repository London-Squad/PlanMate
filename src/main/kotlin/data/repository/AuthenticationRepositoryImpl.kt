package data.repository

import data.fileIO.*
import data.security.hashing.HashingAlgorithm
import logic.entities.User
import logic.exception.AuthenticationException.*
import logic.repositories.AuthenticationRepository
import java.io.File
import java.util.*

class AuthenticationRepositoryImpl(
    private val userFile: File,
    private val hashingAlgorithm: HashingAlgorithm
) : AuthenticationRepository {

    init {
        userFile.createFileIfNotExist("id,userName,password,type\n")
    }

    override fun getMates() = userFile.readLines().map(::lineToUser)

    override fun deleteUser(userId: UUID) {
        val newFileData = userFile.readLines().toMutableList()
        newFileData.removeIf { it.contains("$userId", ignoreCase = true) }
        userFile.clearAndWriteNewData(newFileData)
    }

    override fun login(userName: String, password: String): User {
        val hashedPassword = hashingAlgorithm.hashData(password)
        return userFile.readUserOrNull(userName, hashedPassword) ?: throw UserNotFoundException()
    }

    override fun logout() {
        return
    }

    override fun register(userName: String, password: String): Boolean {
        val hashedPassword = hashingAlgorithm.hashData(password)
        val id = UUID.randomUUID()
        if (userFile.isUserExistInFile(userName, password)) throw UserAlreadyExistException()
        return userFile.writeUser(id, userName, hashedPassword)
    }

    override fun changePassword(userName: String, currentPassword: String, newPassword: String) {
        if (!userFile.isUserExistInFile(userName, currentPassword)) throw UserNotFoundException()
        val newFileData = userFile.readLines().map { line ->
            if (line.contains("$userName,$currentPassword", ignoreCase = true)) {
                line.replace("$userName,$currentPassword", "$userName,$newPassword")
            } else line
        }
        userFile.clearAndWriteNewData(newFileData)
    }
}