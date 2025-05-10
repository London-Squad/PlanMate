package logic.planeMateException

open class RetrievingDataFailureException(message: String) : PlaneMateException(message)


open class NotFoundException(message: String) : RetrievingDataFailureException(message)

class NoLoggedInUserFoundException : NotFoundException("No logged-in user found")

class UserNotFoundException(message: String = "User could not be found") : NotFoundException(message)

class TaskNotFoundException(message: String = "Task could not be found") : NotFoundException(message)

class TaskStateNotFoundException(message: String = "Task state could not be found") : NotFoundException(message)

class ProjectNotFoundException(message: String = "Project could not be found") : NotFoundException(message)

class CorruptedDataException(message: String = "Corrupted data encountered") : RetrievingDataFailureException(message)