package logic.exception

sealed class AuthenticationException(message: String) : Exception(message) {

    class UsernameTakenException : AuthenticationException("Username is already taken")
}