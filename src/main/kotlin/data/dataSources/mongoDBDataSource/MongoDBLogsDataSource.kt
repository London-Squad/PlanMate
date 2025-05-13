package data.dataSources.mongoDBDataSource

import com.mongodb.client.model.Filters
import data.dataSources.mongoDBDataSource.mongoDBParser.MongoDBParser
import data.dataSources.mongoDBDataSource.mongoDBParser.MongoDBQueryHandler
import data.repositories.dtoMappers.toLog
import data.repositories.dtoMappers.toLogDto
import logic.entities.Log
import logic.entities.Project
import logic.exceptions.NotFoundException
import logic.repositories.LogsRepository
import logic.repositories.ProjectsRepository
import logic.repositories.TaskRepository
import logic.repositories.TaskStatesRepository
import java.util.UUID

class MongoDBLogsDataSource(
    private val logQueryHandler: MongoDBQueryHandler,
    private val projectsRepository: ProjectsRepository,
    private val taskRepository: TaskRepository,
    private val taskStatesRepository: TaskStatesRepository,
    private val mongoParser: MongoDBParser
) : LogsRepository {

    override suspend fun getAllLogs(): List<Log> {
        val filters = Filters.empty()
        return logQueryHandler.fetchManyFromCollection(filters).map { doc ->
            mongoParser.documentToLogDto(doc).toLog()
        }
    }

    override suspend fun addLog(log: Log) {
        val doc = mongoParser.logDtoToDocument(log.toLogDto())
        logQueryHandler.insertToCollection(doc)
    }

    override suspend fun getLogsByEntityId(entityId: UUID): List<Log> {
        val relatedEntityIds = mutableSetOf<String>()
        relatedEntityIds.add(entityId.toString())

        val project: Project? = try {
            projectsRepository.getProjectById(entityId)
        } catch (e: NotFoundException) {
            null
        }

        if (project != null) {
            val tasksIds = taskRepository.getTasksByProjectID(project.id, includeDeleted = true).map { it.id.toString() }
            val taskStatesIds = taskStatesRepository.getTaskStatesByProjectId(project.id, includeDeleted = true).map { it.id.toString() }

            relatedEntityIds.addAll(tasksIds)
            relatedEntityIds.addAll(taskStatesIds)
        }

        val filter = Filters.`in`(MongoDBParser.PLAN_ENTITY_ID_FIELD, relatedEntityIds)

        return logQueryHandler.fetchManyFromCollection(filter)
            .map { mongoParser.documentToLogDto(it).toLog() }
            .sortedBy { it.time }
    }
}