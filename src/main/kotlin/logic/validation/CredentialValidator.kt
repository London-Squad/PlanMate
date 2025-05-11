package logic.validation

import logic.exceptions.InvalidPasswordException
import logic.exceptions.InvalidUserNameException

class CredentialValidator {
    fun validateUserName(username: String) {
        if (!username.isValidUserName()) throw InvalidUserNameException(
            "invalid username length, should be from 4 to 11"
        )
    }

    fun validatePassword(password: String) {
        if (!password.isValidPassword()) throw InvalidPasswordException(
            "Password should be between 6 and 12" +
                    "characters and include at least 1 lowercase and 1 uppercase letter"
        )
    }

    private fun String.isValidUserName() = length in 4..11

    private fun String.isValidPassword(): Boolean {
        val passwordRegex = "^(?=.*[a-z])(?=.*[A-Z]).+\$".toRegex()
        return isNotBlank() && length in 6..12 && matches(passwordRegex)
    }
}