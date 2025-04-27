package logic.entity

import java.util.*

sealed class Log (
    val user: User,
    val time: Date,
    val project: Project,
)

class TaskCreationLog(
    user: User,
    time: Date,
    project: Project,
    val createdTask: Task
    ): Log(user = user, time = time, project = project)

class StateCreationLog(
    user: User,
    time: Date,
    project: Project,
    val createdState: State
    ): Log(user = user, time = time, project = project)

class ProjectCreationLog(
    user: User,
    time: Date,
    project: Project,
    ): Log(user = user, time = time, project = project)

class TaskDeletionLog(
    user: User,
    time: Date,
    project: Project,
    val deletedTask: Task
    ): Log(user = user, time = time, project = project)

class StateDeletionLog(
    user: User,
    time: Date,
    project: Project,
    val deletedState: Task
    ): Log(user = user, time = time, project = project)

class ProjectDeletionLog(
    user: User,
    time: Date,
    project: Project,
    ): Log(user = user, time = time, project = project)

class TaskTitleEditLog(
    user: User,
    time: Date,
    project: Project,
    task: Task,
    oldTitle: String,
    newTitle: String,
    ): Log(user = user, time = time, project = project)

class TaskDescriptionEditLog(
    user: User,
    time: Date,
    project: Project,
    task: Task,
    oldDescription: String,
    newDescription: String,
    ): Log(user = user, time = time, project = project)





// user zainab changed task (init project #22) state from InProgress to InDevReview at 2025/05/24 8:00 PM
// user zainab changed task (init project #22) name from (init project) to (init project structure) at 2025/05/24 8:00 PM
// user zainab changed task (init project #22) from created to TO-DO at 2025/05/24 8:00 PM

// user zainab created task (init project #22) at 2025/05/24 8:00 PM
// user zainab deleted task (init project #22) at 2025/05/24 8:00 PM
// user zainab created project (PlanMateV1.0) at 2025/05/24 8:00 PM
