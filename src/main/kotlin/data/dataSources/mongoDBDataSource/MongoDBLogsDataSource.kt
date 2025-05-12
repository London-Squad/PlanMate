package data.dataSources.mongoDBDataSource

import com.mongodb.MongoException
import com.mongodb.kotlin.client.coroutine.MongoCollection
import data.dataSources.mongoDBDataSource.mongoDBParse.MongoDBParse
import data.dto.LogDto
import data.repositories.dataSourceInterfaces.LogsDataSource
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import logic.exceptions.RetrievingDataFailureException
import data.repositories.dtoMappers.toLog
import data.repositories.dtoMappers.toLogDto
import logic.entities.Log
import logic.entities.Project
import logic.exceptions.ProjectNotFoundException
import logic.exceptions.StoringDataFailureException
import logic.repositories.LogsRepository
import logic.repositories.ProjectsRepository
import logic.repositories.TaskRepository
import logic.repositories.TaskStatesRepository
import org.bson.Document
import java.util.UUID

class MongoDBLogsDataSource(
    private val logsCollection: MongoCollection<Document>,
    private val projectsRepository: ProjectsRepository,
    private val taskRepository: TaskRepository,
    private val taskStatesRepository: TaskStatesRepository,
    private val mongoParser: MongoDBParse
) : LogsRepository {

    override suspend fun getAllLogs(): List<Log> {
        return try{
            logsCollection.find().map { doc ->
                mongoParser.documentToLogDto(doc).toLog()
            }.toList()
    } catch (e: MongoException) {
        throw RetrievingDataFailureException("Failed to retrieve logs: ${e.message}")
    }
    }

    override suspend fun addLog(log: Log) {
        try {
            val logDto = log.toLogDto()
            val doc = mongoParser.logDtoToDocument(logDto)
            logsCollection.insertOne(doc)
        } catch (e: MongoException) {
            throw StoringDataFailureException("Failed to add log: ${e.message}")
        }
    }

    override fun getLogsByEntityId(entityId: UUID): List<Log> {
        val relatedEntityIds = mutableSetOf<UUID>()
        relatedEntityIds.add(entityId)

        val project: Project? = try {
            projectsRepository.getProjectById(entityId)
        } catch (e: ProjectNotFoundException) {
            throw ProjectNotFoundException()
        }
        if (project != null) {
            val tasks = taskRepository.getTasksByProjectID(project.id, includeDeleted = true)
            val taskStates = taskStatesRepository.getTaskStatesByProjectId(project.id, includeDeleted = true)

            relatedEntityIds.addAll(tasks.map { it.id })
            relatedEntityIds.addAll(taskStates.map { it.id })
        }
        val filter = Document("\$or", relatedEntityIds.map { id ->
            Document("loggedAction.entityId", id.toString())
        })

        return logsCollection.find(filter)
            .map { mongoParser.documentToLogDto(it).toLog() }
            .sortedBy { it.time }
            .toList()
    }

}