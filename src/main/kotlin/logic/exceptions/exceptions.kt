package logic.exceptions

class InvalidUserNameLengthException :
    AuthenticationException("Username should have more than 3 characters and less than 12")
class UserNameAlreadyTakenException(message: String = "User with this username already exists") :
    AuthenticationException(message)


open class DataSourceAccessException(message: String) : Exception(message)
class StoringDataFailureException(message: String) : DataSourceAccessException(message)
open class RetrievingDataFailureException(message: String) : DataSourceAccessException(message)

open class NotFoundException(message: String) : RetrievingDataFailureException(message)
class NoLoggedInUserFoundException : NotFoundException("No logged-in user found")
class UserNotFoundException(message: String = "User could not be found") : NotFoundException(message)
class TaskNotFoundException(message: String = "Task could not be found") : NotFoundException(message)
class TaskStateNotFoundException(message: String = "Task state could not be found") : NotFoundException(message)
class ProjectNotFoundException(message: String = "Project could not be found") : NotFoundException(message)


open class AuthenticationException(message: String) : Exception(message)
class InvalidPasswordException(message: String) : AuthenticationException(message)
