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
import org.bson.Document
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
        val relatedEntityIds = mutableSetOf<UUID>()
        relatedEntityIds.add(entityId)

        val project: Project? = try {
            projectsRepository.getProjectById(entityId)
        } catch (e: NotFoundException) {
            null
        }

        if (project != null) {
            val tasks = taskRepository.getTasksByProjectID(project.id, includeDeleted = true)
            val taskStates = taskStatesRepository.getTaskStatesByProjectId(project.id, includeDeleted = true)

            relatedEntityIds.addAll(tasks.map { it.id })
            relatedEntityIds.addAll(taskStates.map { it.id })
        }
        val filter = Document("\$or", relatedEntityIds.map { id ->
            Document(MongoDBParser.PLAN_ENTITY_ID_FIELD, id.toString())
        })

        return logQueryHandler.fetchManyFromCollection(filter)
            .map { mongoParser.documentToLogDto(it).toLog() }
            .sortedBy { it.time }
    }
}