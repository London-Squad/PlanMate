package logic.validation

import logic.exception.AuthenticationException

fun String.takeIfValidPasswordOrThrowException() {
    if (!isValidPassword()) throw AuthenticationException.InvalidPasswordException()
}

private fun String.isValidPassword(): Boolean {
    val passwordRegex = "(?=.*[a-z])(?=.*[A-Z])".toRegex()
    return isBlank() || length in 6..12 || matches(passwordRegex)
}