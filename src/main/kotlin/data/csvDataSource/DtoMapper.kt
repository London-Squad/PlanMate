package data.csvDataSource

import data.dto.*
import logic.entities.*
import java.util.*

class DtoMapper {
    fun mapToTask(taskDto: TaskDto, taskState: TaskState): Task {
        return Task(
            id = taskDto.id,
            title = taskDto.title,
            description = taskDto.description,
            taskState = taskState
        )
    }

    fun mapToTaskDto(task: Task, projectId: UUID, isDeleted: Boolean = false): TaskDto {
        return TaskDto(
            id = task.id,
            title = task.title,
            description = task.description,
            stateId = task.taskState.id,
            projectId = projectId,
            isDeleted = isDeleted
        )
    }

    fun mapToProject(projectDto: ProjectDto, tasks: List<Task>, tasksStates: List<TaskState>): Project {
        return Project(
            id = projectDto.id,
            title = projectDto.title,
            description = projectDto.description,
            tasks = tasks,
            tasksStates = tasksStates
        )
    }

    fun mapToProjectDto(project: Project, isDeleted: Boolean = false): ProjectDto {
        return ProjectDto(
            id = project.id,
            title = project.title,
            description = project.description,
            isDeleted = isDeleted
        )
    }

    fun mapToTaskState(taskStateDto: TaskStateDto): TaskState {
        return TaskState(
            id = taskStateDto.id,
            title = taskStateDto.title,
            description = taskStateDto.description
        )
    }

    fun mapToTaskStateDto(taskState: TaskState, projectId: UUID, isDeleted: Boolean = false): TaskStateDto {
        return TaskStateDto(
            id = taskState.id,
            title = taskState.title,
            description = taskState.description,
            projectId = projectId,
            isDeleted = isDeleted
        )
    }

    fun mapToUser(userDto: UserDto): User {
        return User(
            id = userDto.id,
            userName = userDto.userName,
            type = User.Type.valueOf(userDto.type.uppercase())
        )
    }

    fun mapToUserDto(user: User, hashedPassword: String, isDeleted: Boolean = false): UserDto {
        return UserDto(
            id = user.id,
            userName = user.userName,
            hashedPassword = hashedPassword,
            type = user.type.name,
            isDeleted = isDeleted
        )
    }

    fun mapToLog(logDto: LogDto, user: User, planEntity: PlanEntity): Log {

        val action = when (logDto.action.lowercase()) {
            "create" -> Create(entity = planEntity)
            "delete" -> Delete(entity = planEntity)
            "edit" -> Edit(
                entity = planEntity,
                property = logDto.planEntityProperty,
                oldValue = logDto.oldValue,
                newValue = logDto.newValue
            )

            else -> throw IllegalArgumentException("Unknown action type: ${logDto.action}")
        }

        return Log(
            id = logDto.id,
            user = user,
            time = logDto.time,
            action = action
        )
    }

    fun mapToLogDto(log: Log): LogDto {
        val action = when (log.action) {
            is Create -> "create"
            is Delete -> "delete"
            is Edit -> "edit"
        }

        return LogDto(
            id = log.id,
            userId = log.user.id,
            time = log.time,
            action = action,
            planEntityId = log.action.entity.id,
            planEntityProperty = if (log.action is Edit) log.action.property else "Nan",
            oldValue = if (log.action is Edit) log.action.oldValue else "Nan",
            newValue = if (log.action is Edit) log.action.newValue else "Nan"
        )
    }
}