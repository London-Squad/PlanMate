package logic.entity

import java.util.*

sealed class Log (
    val user: User,
    val time: Date,
    val project: Project,
)

class CreateTaskLog(
    user: User,
    time: Date,
    project: Project,
    val createdTask: Task
    ): Log(user = user, time = time, project = project)

class CreateProjectLog(
    user: User,
    time: Date,
    project: Project,
    ): Log(user = user, time = time, project = project)

class deleteTaskLog(
    user: User,
    time: Date,
    project: Project,
    val deletedTask: Task
    ): Log(user = user, time = time, project = project)