package logic.exception

sealed class AuthenticationException(message: String) : Exception(message) {
    class InvalidUserNameLengthException : AuthenticationException("Username should be 4 or more characters")


    class UserNotFoundException : AuthenticationException("User Not found")

    class UserAlreadyExistException : AuthenticationException("User Already exist")
}