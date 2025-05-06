package logic.exceptions.authenticationExceptions

import logic.exceptions.PlaneMateException

open class AuthenticationException(message: String) : PlaneMateException(message)

class InvalidUserNameLengthException : AuthenticationException("Username should be 4 or more characters")
class InvalidPasswordException :
    AuthenticationException("Password should be 6 to 12 characters and include at least 1 lowercase and 1 uppercase letter")

class UseNameAlreadyExistException : AuthenticationException("A user with this username already exists")
class UnauthorizedAccessException : AuthenticationException("You do not have permission to perform this action!")
class RegistrationFailedException :
    AuthenticationException("Unable to register user, please try again or contact support.")
