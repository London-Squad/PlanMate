package logic.validation

import logic.exceptions.InvalidPasswordException
import logic.exceptions.InvalidUserNameLengthException

class CredentialValidator {
    fun takeIfValidNameOrThrowException(username: String) {
        if (!username.isValidUserName()) throw InvalidUserNameLengthException()
    }

    fun takeIfValidPasswordOrThrowException(password: String) {
        if (!password.isValidPassword()) throw InvalidPasswordException()
    }

    private fun String.isValidUserName() = length in 4..11

    private fun String.isValidPassword(): Boolean {
        val passwordRegex = "^(?=.*[a-z])(?=.*[A-Z]).+\$".toRegex()
        return isNotBlank() && length in 6..12 && matches(passwordRegex)
    }
}