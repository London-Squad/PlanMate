package data.dataSources.mongoDBDataSource

import com.mongodb.MongoException
import com.mongodb.kotlin.client.coroutine.MongoCollection
import data.dataSources.mongoDBDataSource.mongoDBParse.MongoDBParse
import data.dto.LogMongoDto
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import logic.exceptions.RetrievingDataFailureException
import data.repositories.dtoMappers.toLog
import data.repositories.dtoMappers.toLogDto
import data.repositories.dtoMappers.toLogMongoDto
import logic.entities.*
import logic.exceptions.ProjectNotFoundException
import logic.exceptions.StoringDataFailureException
import logic.repositories.LogsRepository
import logic.repositories.ProjectsRepository
import logic.repositories.TaskRepository
import logic.repositories.TaskStatesRepository
import org.bson.Document
import java.time.LocalDateTime
import java.util.UUID

class MongoDBLogsDataSource(
    private val logsCollection: MongoCollection<LogMongoDto>,
    private val projectsRepository: ProjectsRepository,
    private val taskRepository: TaskRepository,
    private val taskStatesRepository: TaskStatesRepository
) : LogsRepository {

    override suspend fun getAllLogs(): List<Log> {
        return try {
            logsCollection.find()
                .map(LogMongoDto::toLog)
                .toList()
        } catch (e: MongoException) {
            throw RetrievingDataFailureException("Failed to retrieve logs: ${e.message}")
        }
    }

    override suspend fun addLog(log: Log) {
        try {
            logsCollection.insertOne(log.toLogMongoDto())
        } catch (e: MongoException) {
            throw StoringDataFailureException("Failed to add log: ${e.message}")
        }
    }

    override suspend fun getLogsByEntityId(entityId: UUID): List<Log> {
        val relatedEntityIds = mutableSetOf<UUID>()
        relatedEntityIds.add(entityId)

        val project: Project? = try {
            projectsRepository.getProjectById(entityId)
        } catch (e: ProjectNotFoundException) {
            null
        }

        if (project != null) {
            val tasks = taskRepository.getTasksByProjectID(project.id, includeDeleted = true)
            val taskStates = taskStatesRepository.getTaskStatesByProjectId(project.id, includeDeleted = true)

            relatedEntityIds.addAll(tasks.map { it.id })
            relatedEntityIds.addAll(taskStates.map { it.id })
        }
        val filter = Document("\$or", relatedEntityIds.map { id ->
            Document("_id", id.toString())
        })

        return logsCollection.find(filter)
            .map { doc ->
                val action = when (doc.action.lowercase()) {
                    "create" -> EntityCreationLog(entityId = UUID.fromString(doc.planEntityId))
                    "delete" -> EntityDeletionLog(entityId = UUID.fromString(doc.planEntityId))
                    "edit" -> EntityEditionLog(
                        entityId = UUID.fromString(doc.planEntityId),
                        property = doc.planEntityProperty,
                        oldValue = doc.oldValue,
                        newValue = doc.newValue
                    )

                    else -> throw RetrievingDataFailureException("Unknown action type: ${doc.action}")
                }
                Log(UUID.fromString(doc.id), UUID.fromString(doc.userId), LocalDateTime.parse(doc.time), action)
            }
            .toList()
            .sortedBy { it.time }
    }
}