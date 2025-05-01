package logic.exception

sealed class AuthenticationException(message: String) : Exception(message) {
    class InvalidUserNameLengthException : AuthenticationException("Username should be 4 or more characters")

    class InvalidPasswordException : AuthenticationException(
        "Password should be 6 to 12 character and includes at least 1 lower case and 1 uppercase character"
    )

    class UserNotFoundException : AuthenticationException("User Not found")

    class UserAlreadyExistException : AuthenticationException("User Already exist")
}