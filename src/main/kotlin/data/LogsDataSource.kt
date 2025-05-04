package data

import data.fileIO.cvsLogsFileHandler.LogsCsvReader
import data.fileIO.cvsLogsFileHandler.LogsCsvWriter
import logic.entities.*
import logic.repositories.*
import java.time.LocalDateTime
import java.util.UUID

class LogsDataSource(
    private val logsCsvReader: LogsCsvReader,
    private val logsCsvWriter: LogsCsvWriter,
    private val cacheDataRepository: CacheDataRepository,
    private val projectsRepository: ProjectsRepository
) : LogsRepository {

    override fun getAllLogs(): List<Log> {
        return logsCsvReader.readRows().map { parseLogCsvRowToLogProjectIdPair(it).first }
    }

    override fun getLogsByEntityId(entityId: UUID): List<Log> {
        return logsCsvReader.readRows()
            .map(::parseLogCsvRowToLogProjectIdPair)
            .filter { (log, projectId) -> log.planEntityId == entityId || projectId == entityId }
            .map { it.first }
    }

    override fun addCreationLog(planEntity: PlanEntity) {
        val loggerInUser = cacheDataRepository.getLoggedInUser()
        addLog(
            Log(
                userId = loggerInUser.id,
                planEntityId = planEntity.id,
                message = "${loggerInUser.userName} created ${getEntityType(planEntity)} (${planEntity.title})"
            )
        )
    }

    override fun addDeletionLog(planEntity: PlanEntity) {
        val loggerInUser = cacheDataRepository.getLoggedInUser()
        addLog(
            Log(
                userId = loggerInUser.id,
                planEntityId = planEntity.id,
                message = "${loggerInUser.userName} deleted ${getEntityType(planEntity)} (${planEntity.title})"
            )
        )
    }

    override fun addEditionLog(
        planEntity: PlanEntity,
        planEntityPropertyToChange: String,
        oldValue: String,
        newValue: String
    ) {
        val loggerInUser = cacheDataRepository.getLoggedInUser()

        addLog(
            Log(
                userId = loggerInUser.id,
                planEntityId = planEntity.id,
                message = "${loggerInUser.userName} edited the $planEntityPropertyToChange of ${getEntityType(planEntity)} (${planEntity.title}) from (${oldValue}) to (${newValue})"
            )
        )
    }

    private fun getEntityType(planEntity: PlanEntity): String {
        return when (planEntity) {
            is Project -> "project"
            is State -> "state"
            is Task -> "task"
            else -> "unknown plan entity"
        }
    }

    private fun addLog(log: Log) {
        logsCsvWriter.appendLine(
            logObjToCsvRow(log)
        )
    }

    private fun logObjToCsvRow(log: Log): String {
        return listOf(
            log.id.toString(),
            log.time.toString(),
            log.userId.toString(),
            log.planEntityId.toString(),
            log.message,
            getProjectIdByPlanEntityId(log.planEntityId)
        ).joinToString(",")
    }

    private fun parseLogCsvRowToLogProjectIdPair(line: String): Pair<Log, UUID> {
        val parts = line.split(",")
        val projectId = UUID.fromString(parts[5])

        return Pair(
            Log(
                id = UUID.fromString(parts[0]),
                time = LocalDateTime.parse(parts[1]),
                userId = UUID.fromString(parts[2]),
                planEntityId = UUID.fromString(parts[3]),
                message = parts[4]
            ),
            projectId
        )
    }

    private fun getProjectIdByPlanEntityId(planEntityId: UUID): UUID {
        return projectsRepository.getAllProjects().first { project ->
            project.id == planEntityId ||
                    project.tasks.any { it.id == planEntityId } ||
                    project.states.any { it.id == planEntityId }
        }.id
    }
}

