package data.repositoriesImpl

import data.dataSources.*
import data.csvDataSource.DtoMapper
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
    private val tasksStatesDataSource: TasksStatesDataSource,
    private val taskRepository: TaskRepository,
    private val usersDataSource: UsersDataSource,
    private val mapper: DtoMapper
) : LogsRepository {
    override fun getAllLogs(): List<Log> {
        return logsDataSource.getAllLogs()
            .map { mapper.mapToLog(it, getUserById(it.userId), getEntityById(it.planEntityId)) }
    }

    private fun getUserById(userId: UUID): User {
        val admin = usersDataSource.getAdmin()
        if (userId == admin.id) {
            return mapper.mapToUser(admin)
        }
        return usersDataSource.getMates().first() { it.id == userId }
            .let(mapper::mapToUser)
    }

    private fun getEntityById(entityId: UUID): PlanEntity {
        return projectsRepository.getAllProjects(includeDeleted = true)
            .firstOrNull { it.id == entityId }

            ?: tasksStatesDataSource.getAllTasksStates()
                .firstOrNull { it.id == entityId }
                ?.let { mapper.mapToTaskState(it) }

            ?: taskRepository.getTaskByID(entityId, includeDeleted = true)
    }

    override fun getLogsByEntityId(entityId: UUID): List<Log> {
        var result: List<Log>
        result = getAllLogs().filter { it.action.entity.id == entityId }

        result.forEach { log ->
            if (log.action.entity is Project) {
                (log.action.entity as Project).tasks.forEach { task ->
                    result = result + getLogsByEntityId(task.id)
                }
                (log.action.entity as Project).tasksStates.forEach { state ->
                    result = result + getLogsByEntityId(state.id)
                }
            }
        }

        result = result.toSet().toList()

        result = result.sortedBy { it.time }

        return result
    }

    override fun addLog(log: Log) {
        logsDataSource.addLog(mapper.mapToLogDto(log))
    }
}