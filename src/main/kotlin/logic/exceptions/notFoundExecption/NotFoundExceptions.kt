package logic.exceptions.notFoundExecption

import logic.exceptions.PlaneMateException

open class NotFoundException(message: String) : PlaneMateException(message)

class UserNotFoundException : NotFoundException("User could not be found")

class TaskNotFoundException : NotFoundException("Task could not be found")

class TaskStateNotFoundException : NotFoundException("Task state could not be found")

class ProjectNotFoundException : NotFoundException("Project could not be found")
