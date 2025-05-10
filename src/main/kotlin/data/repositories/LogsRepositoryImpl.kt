package data.repositories

import data.repositories.dataSourceInterfaces.LogsDataSource
import data.repositories.dataSourceInterfaces.TaskStatesDataSource
import data.repositories.dataSourceInterfaces.UsersDataSource
import data.repositories.dtoMappers.toLog
import data.repositories.dtoMappers.toLogDto
import logic.entities.Log
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
    override fun getAllLogs(): List<Log> {
        return logsDataSource.getAllLogs()
            .map { it.toLog() }
    }


    override fun getLogsByEntityId(entityId: UUID): List<Log> {
//        var result: List<Log>
//        result = getAllLogs().filter { it.loggedAction.entityId == entityId }
//
//        result.forEach { log ->
//            if (log.loggedAction.entity is Project) {
//                (log.loggedAction.entity as Project).tasks.forEach { task ->
//                    result = result + getLogsByEntityId(task.id)
//                }
//                (log.loggedAction.entity as Project).tasksStates.forEach { state ->
//                    result = result + getLogsByEntityId(state.id)
//                }
//            }
//        }
//
//        result = result.toSet().toList()
//
//        result = result.sortedBy { it.time }
        //todo: should be seperated in multiple functions ByProjectId, ByTaskId, ByTaskStateId
        return listOf()
    }

    override fun addLog(log: Log) {
        logsDataSource.addLog(log.toLogDto())
    }
}