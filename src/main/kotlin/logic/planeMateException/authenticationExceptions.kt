package logic.planeMateException

open class AuthenticationException(message: String) : PlaneMateException(message)

class InvalidUserNameLengthException : AuthenticationException(
    "Username should be between ${AuthenticationConstraints.MIN_USERNAME_LENGTH} and ${AuthenticationConstraints.MAX_USERNAME_LENGTH} characters"
)

class InvalidPasswordException : AuthenticationException(
    "Password should be between ${AuthenticationConstraints.MIN_PASSWORD_LENGTH} and ${AuthenticationConstraints.MAX_PASSWORD_LENGTH} " +
            "characters and include at least 1 lowercase and 1 uppercase letter"
)

class UserNameAlreadyExistException : AuthenticationException("A user with this username already exists")

class UnauthorizedAccessException : AuthenticationException("You do not have permission to perform this action!")

class RegistrationFailedException : AuthenticationException("Unable to register user, please try again or contact support.")
