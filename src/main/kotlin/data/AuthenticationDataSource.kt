package data

import data.fileIO.UserFileHelper
import data.fileIO.createFileIfNotExist
import data.security.hashing.HashingAlgorithm
import logic.entities.User
import logic.exceptions.NoLoggedInUserIsSavedInCacheException
import logic.exceptions.UserAlreadyExistException
import logic.exceptions.UserNotFoundException
import logic.repositories.AuthenticationRepository
import java.io.File
import java.util.*

class AuthenticationDataSource(
    private val userFile: File,
    private val activeUserFile: File,
    private val hashingAlgorithm: HashingAlgorithm
) : AuthenticationRepository {
    private var loggedInUser: User? = null

    init {
        userFile.createFileIfNotExist("id,userName,password,type\n")
        activeUserFile.createFileIfNotExist("")
        loggedInUser = loadUserFromLocalFile()
    }

    override fun getMates(): List<User> {
        val users = userFile.readLines().toMutableList()
        users.removeFirst()
        return users.map(UserFileHelper::lineToUser)
    }

    override fun deleteUser(userId: UUID) {
        val newFileData = userFile.readLines().toMutableList()
        newFileData.removeIf { it.contains("$userId", ignoreCase = true) }
        UserFileHelper.clearAndWriteNewData(userFile, newFileData)
    }

    override fun login(userName: String, password: String): User {
        if (userName == ADMIN.userName && password == ADMIN_PASSWORD) return ADMIN
        val hashedPassword = hashingAlgorithm.hashData(password)
        return UserFileHelper.readUserOrNull(userFile, userName, hashedPassword) ?: throw UserNotFoundException()
    }

    override fun logout() = true

    override fun register(userName: String, password: String): Boolean {
        val hashedPassword = hashingAlgorithm.hashData(password)
        if (UserFileHelper.isUserNameExistInFile(userFile, userName)) throw UserAlreadyExistException()
        val id = UUID.randomUUID()
        return UserFileHelper.writeUser(userFile, id, userName, hashedPassword)
    }

    override fun changePassword(userName: String, currentPassword: String, newPassword: String): Boolean {
        if (!UserFileHelper.isUserExistInFile(userFile, userName, currentPassword)) throw UserNotFoundException()
        val newFileData = userFile.readLines().map { line ->
            if (line.contains("$userName,$currentPassword", ignoreCase = true)) {
                line.replace("$userName,$currentPassword", "$userName,$newPassword")
            } else line
        }
        UserFileHelper.clearAndWriteNewData(userFile, newFileData)
        return true
    }

    override fun getLoggedInUser(): User {
        if (loggedInUser == null) throw NoLoggedInUserIsSavedInCacheException()
        return loggedInUser!!
    }

    override fun setLoggedInUser(user: User) {
        activeUserFile.writeText("${user.id},${user.userName},${user.type}")
        loggedInUser = user
    }

    override fun clearLoggedInUserFromCache() {
        activeUserFile.writeText("")
        loggedInUser = null
    }

    private fun loadUserFromLocalFile(): User? {
        val text = activeUserFile.readText().trim()
        if (text.isEmpty()) return null
        return text.split(",")
            .run { User(this[0].toUUID(), this[1], getUserTypeFromString(this[2])) }
    }

    private fun String.toUUID(): UUID {
        return UUID.fromString(this)
    }

    private fun getUserTypeFromString(type: String): User.Type {
        return when (type.lowercase()) {
            "admin" -> User.Type.ADMIN
            else -> User.Type.MATE
        }
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