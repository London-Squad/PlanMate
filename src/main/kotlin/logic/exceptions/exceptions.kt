package logic.exceptions

open class AuthenticationException(message: String) : Exception(message)

class InvalidUserNameLengthException : AuthenticationException("Username should be 4 or more characters")
class InvalidPasswordException : AuthenticationException("Invalid password")
class UserAlreadyExistException : AuthenticationException("User Already exist")
class UnauthorizedAccessException : AuthenticationException("Unauthorized Access")
class UserNameAlreadyTakenException : AuthenticationException("Username Taken Exception")
class RegistrationFailedException : AuthenticationException("Registration Failed Exception")


open class NotFoundException(message: String) : Exception(message)
class NoLoggedInUserFoundException() : Exception("no logged in user found")
class UserNotFoundException : NotFoundException("User Not found")
class TaskNotFoundException : NotFoundException("Task Not found")
class TaskStateNotFoundException : NotFoundException("TaskState Not found")
class ProjectNotFoundException(message: String = "Project Not found") : NotFoundException(message)


class RetrievingDataFailureException(message: String) : Exception(message)