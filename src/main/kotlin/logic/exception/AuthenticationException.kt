package logic.exception

sealed class AuthenticationException(message: String) : Exception(message) {
    class InvalidUserNameLengthException : AuthenticationException("Username should be 4 or more characters")
    class UserNotFoundException : AuthenticationException("User Not found")
    class UserAlreadyExistException : AuthenticationException("User Already exist")
    class UnauthorizedAccessException : AuthenticationException("Only admins can access mate management")
    class UsernameTakenException : AuthenticationException("Username is already taken")
    class InvalidPasswordException : AuthenticationException("Password must be 8 or more characters")
    class RegistrationFailedException: AuthenticationException("Failed to save user data, Try Later !")

}
