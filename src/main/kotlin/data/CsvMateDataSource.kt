package data

import data.fileIO.UserFileHelper
import data.security.hashing.HashingAlgorithm
import logic.entities.User
import logic.exceptions.UserAlreadyExistException
import logic.exceptions.UserNotFoundException
import logic.repositories.MateRepository
import logic.validation.CredentialValidator
import java.io.File
import java.util.*

class CsvMateDataSource(
    private val userFile: File,
    private val hashingAlgorithm: HashingAlgorithm,
    private val credentialValidator: CredentialValidator
): MateRepository {

    override fun addMate(userName: String, password: String): Boolean {
        if (UserFileHelper.isUserNameExistInFile(userFile, userName)) throw UserAlreadyExistException()
        val hashedPassword = hashingAlgorithm.hashData(password)
        val id = UUID.randomUUID()
        return UserFileHelper.writeUser(userFile, id, userName, hashedPassword)
    }

    override fun changeMatePassword(userName: String, currentPassword: String, newPassword: String): Boolean {
        validateChangePasswordInputs(userName, currentPassword, newPassword)
        val newFileData = getNewFileData(userName, currentPassword, newPassword)
        UserFileHelper.clearAndWriteNewData(userFile, newFileData)
        return true
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

    private fun validateChangePasswordInputs(userName: String, currentPassword: String, newPassword: String) {
        credentialValidator.takeIfValidNameOrThrowException(userName)
        credentialValidator.takeIfValidPasswordOrThrowException(currentPassword)
        credentialValidator.takeIfValidPasswordOrThrowException(newPassword)
        if (!UserFileHelper.isUserExistInFile(userFile, userName, currentPassword)) throw UserNotFoundException()
    }

    private fun getNewFileData(userName: String, currentPassword: String, newPassword: String): List<String> {
        return userFile.readLines().map { line ->
            if (line.contains("$userName,$currentPassword", ignoreCase = true)) {
                line.replace("$userName,$currentPassword", "$userName,$newPassword")
            } else line
        }
    }
}