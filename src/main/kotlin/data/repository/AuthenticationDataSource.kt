package data.repository

import data.fileIO.*
import data.security.hashing.HashingAlgorithm
import logic.entities.User
import logic.exception.AuthenticationException.*
import logic.repositories.AuthenticationRepository
import java.io.File
import java.util.*

class AuthenticationDataSource(
    private val userFile: File,
    private val hashingAlgorithm: HashingAlgorithm
) : AuthenticationRepository {

    init {
        userFile.createFileIfNotExist("id,userName,password,type\n")
    }

    override fun getMates(): List<User> {
        val users = userFile.readLines().toMutableList()
        users.removeFirst()
        return users.map(::lineToUser)
    }

    override fun deleteUser(userId: UUID) {
        val newFileData = userFile.readLines().toMutableList()
        newFileData.removeIf { it.contains("$userId", ignoreCase = true) }
        userFile.clearAndWriteNewData(newFileData)
    }

    override fun login(userName: String, password: String): User {
        if (userName == ADMIN.userName && password == ADMIN_PASSWORD) return ADMIN
        val hashedPassword = hashingAlgorithm.hashData(password)
        return userFile.readUserOrNull(userName, hashedPassword) ?: throw UserNotFoundException()
    }

    override fun logout() = true

    override fun register(userName: String, password: String): Boolean {
        val hashedPassword = hashingAlgorithm.hashData(password)
        if (userFile.isUserExistInFile(userName, password)) throw UserAlreadyExistException()
        val id = UUID.randomUUID()
        return userFile.writeUser(id, userName, hashedPassword)
    }

    override fun changePassword(userName: String, currentPassword: String, newPassword: String): Boolean {
        if (!userFile.isUserExistInFile(userName, currentPassword)) throw UserNotFoundException()
        val newFileData = userFile.readLines().map { line ->
            if (line.contains("$userName,$currentPassword", ignoreCase = true)) {
                line.replace("$userName,$currentPassword", "$userName,$newPassword")
            } else line
        }
        userFile.clearAndWriteNewData(newFileData)
        return true
    }

    private companion object {
        val ADMIN = User(
            id = UUID.fromString("5750f82c-c1b6-454d-b160-5b14857bc9dc"),
            userName = "admin",
            type = User.Type.ADMIN
        )
        const val ADMIN_PASSWORD = "Admin12"
    }
}