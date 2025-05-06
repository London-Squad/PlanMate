package logic.exceptions.notFoundExecption

import logic.exceptions.PlaneMateException

open class NotFoundException(message: String) : PlaneMateException(message)

class UserNotFoundException(message: String = "User could not be found") : NotFoundException(message)

class TaskNotFoundException(message: String = "Task could not be found") : NotFoundException(message)

class TaskStateNotFoundException(message: String = "Task state could not be found") : NotFoundException(message)

class ProjectNotFoundException(message: String = "Project could not be found") : NotFoundException(message)
