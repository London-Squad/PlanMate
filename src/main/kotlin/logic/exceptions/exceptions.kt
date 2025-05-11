package logic.exceptions

open class AuthenticationException(message: String) : Exception(message)

class InvalidUserNameLengthException :
    AuthenticationException("Username should have more than 3 characters and less than 12")
class InvalidPasswordException : AuthenticationException("Invalid password")
class UserNameAlreadyTakenException(message: String = "User with this username already exists") :
    AuthenticationException(message)

open class RetrievingDataFailureException(message: String) : Exception(message)
open class StoringDataFailureException(message: String) : Exception(message)

open class NotFoundException(message: String) : RetrievingDataFailureException(message)
class NoLoggedInUserFoundException() : NotFoundException("no logged in user found")
class UserNotFoundException(message: String = "User Not found") : NotFoundException(message)
class TaskNotFoundException(message: String = "Task Not found") : NotFoundException(message)
class TaskStateNotFoundException(message: String = "TaskState Not found") : NotFoundException(message)
class ProjectNotFoundException(message: String = "Project Not found") : NotFoundException(message)