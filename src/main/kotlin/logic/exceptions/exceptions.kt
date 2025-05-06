package logic.exceptions

class NoLoggedInUserIsSavedInCacheException() : Exception("no logged in user is saved in memory")


open class AuthenticationException(message: String) : Exception(message)

class InvalidUserNameLengthException : AuthenticationException("Username should be 4 or more characters")

class InvalidPasswordException : AuthenticationException(
    "Password should be 6 to 12 character and includes at least 1 lower case and 1 uppercase character"
)


class UserAlreadyExistException : AuthenticationException("User Already exist")

class UnauthorizedAccessException : AuthenticationException("UnauthorizedAccessException")
class UsernameTakenException : AuthenticationException("UsernameTakenException")
class RegistrationFailedException : AuthenticationException("RegistrationFailedException")

open class NotFoundException(message: String) : Exception(message)
class UserNotFoundException : NotFoundException("User Not found")
class TaskNotFoundException : NotFoundException("Task Not found")
class TaskStateNotFoundException : NotFoundException("TaskState Not found")
class ProjectNotFoundException(message: String = "Project Not found") : NotFoundException(message)

class RetrievingDataFailureException(message: String) : Exception(message)