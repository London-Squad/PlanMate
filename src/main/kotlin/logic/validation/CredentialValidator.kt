package logic.validation

import logic.exceptions.InvalidPasswordException
import logic.exceptions.InvalidUserNameLengthException

class CredentialValidator {
    fun validateUserName(username: String) {
        if (!username.isValidUserName()) throw InvalidUserNameLengthException()
    }

    fun validatePassword(password: String) {
        if (!password.isValidPassword()) throw InvalidPasswordException()
    }

    private fun String.isValidUserName() = length in 4..11

    private fun String.isValidPassword(): Boolean {
        val passwordRegex = "^(?=.*[a-z])(?=.*[A-Z]).+\$".toRegex()
        return isNotBlank() && length in 6..12 && matches(passwordRegex)
    }
}