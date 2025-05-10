package data.repositories

import data.repositories.dataSourceInterfaces.LogsDataSource
import data.repositories.dataSourceInterfaces.TaskStatesDataSource
import data.repositories.dataSourceInterfaces.UsersDataSource
import data.repositories.dtoMappers.toLog
import data.repositories.dtoMappers.toLogDto
import data.repositories.dtoMappers.toTaskState
import data.repositories.dtoMappers.toUser
import logic.entities.Log
import logic.entities.PlanEntity
import logic.entities.Project
import logic.entities.User
import logic.repositories.LogsRepository
import logic.repositories.ProjectsRepository
import logic.repositories.TaskRepository
import java.util.*

class LogsRepositoryImpl(
    private val logsDataSource: LogsDataSource,
    private val projectsRepository: ProjectsRepository,
    private val taskStatesDataSource: TaskStatesDataSource,
    private val taskRepository: TaskRepository,
    private val usersDataSource: UsersDataSource
) : LogsRepository {
    override suspend fun getAllLogs(): List<Log> {
        return logsDataSource.getAllLogs()
            .map { it.toLog(getUserById(it.userId), getEntityById(it.planEntityId)) }
    }

    private suspend fun getUserById(userId: UUID): User {
        val admin = usersDataSource.getAdmin()
        if (userId == admin.id) {
            return admin.toUser()
        }
        return usersDataSource.getMates().first { it.id == userId }.toUser()
    }

    private suspend fun getEntityById(entityId: UUID): PlanEntity {
        return projectsRepository.getAllProjects(includeDeleted = true)
            .firstOrNull { it.id == entityId }

            ?: taskStatesDataSource.getAllTasksStates(true)
                        .firstOrNull { it.id == entityId }?.toTaskState()

            ?: taskRepository.getTaskByID(entityId, includeDeleted = true)
    }

    override suspend fun getLogsByEntityId(entityId: UUID): List<Log> {
        var result: List<Log>
        result = getAllLogs().filter { it.loggedAction.entity.id == entityId }

        result.forEach { log ->
            if (log.loggedAction.entity is Project) {
                (log.loggedAction.entity as Project).tasks.forEach { task ->
                    result = result + getLogsByEntityId(task.id)
                }
                (log.loggedAction.entity as Project).tasksStates.forEach { state ->
                    result = result + getLogsByEntityId(state.id)
                }
            }
        }

        result = result.toSet().toList()

        result = result.sortedBy { it.time }

        return result
    }

    override suspend fun addLog(log: Log) {
        logsDataSource.addLog(log.toLogDto())
    }
}