package data.entitiesData

import logic.entities.*
import java.util.*

class DtoMapper {
    fun mapToTask(taskData: TaskData, taskState: State): Task {
        return Task(
            id = taskData.id,
            title = taskData.title,
            description = taskData.description,
            state = taskState
        )
    }

    fun mapToTaskData(task: Task, projectId: UUID, isDeleted: Boolean = false): TaskData {
        return TaskData(
            id = task.id,
            title = task.title,
            description = task.description,
            stateId = task.state.id,
            projectId = projectId,
            isDeleted = isDeleted
        )
    }

    fun mapToProject(projectData: ProjectData, tasks: List<Task>, states: List<State>): Project {
        return Project(
            id = projectData.id,
            title = projectData.title,
            description = projectData.description,
            tasks = tasks,
            states = states
        )
    }

    fun mapToProjectData(project: Project, isDeleted: Boolean = false): ProjectData {
        return ProjectData(
            id = project.id,
            title = project.title,
            description = project.description,
            isDeleted = isDeleted
        )
    }

    fun mapToTaskState(taskStateData: TaskStateData): State {
        return State(
            id = taskStateData.id,
            title = taskStateData.title,
            description = taskStateData.description
        )
    }

    fun mapToTaskStateData(state: State, projectId: UUID, isDeleted: Boolean = false): TaskStateData {
        return TaskStateData(
            id = state.id,
            title = state.title,
            description = state.description,
            projectId = projectId,
            isDeleted = isDeleted
        )
    }

    fun mapToUser(userData: UserData): User {
        return User(
            id = userData.id,
            userName = userData.userName,
            type = User.Type.valueOf(userData.type.uppercase())
        )
    }

    fun mapToUserData(user: User, hashedPassword: String, isDeleted: Boolean = false): UserData {
        return UserData(
            id = user.id,
            userName = user.userName,
            hashedPassword = hashedPassword,
            type = user.type.name,
            isDeleted = isDeleted
        )
    }

    fun mapToLog(logData: LogData, user: User, planEntity: PlanEntity): Log {

        val action = when (logData.action.lowercase()) {
            "create" -> Create(entity = planEntity)
            "delete" -> Delete(entity = planEntity)
            "edit" -> Edit(
                entity = planEntity,
                property = logData.planEntityProperty,
                oldValue = logData.oldValue,
                newValue = logData.newValue
            )

            else -> throw IllegalArgumentException("Unknown action type: ${logData.action}")
        }

        return Log(
            id = logData.id,
            user = user,
            time = logData.time,
            action = action
        )
    }

    fun mapToLogData(log: Log): LogData {
        val action = when (log.action) {
            is Create -> "create"
            is Delete -> "delete"
            is Edit -> "edit"
        }

        return LogData(
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