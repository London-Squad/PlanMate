package data.dataSources.mongoDBDataSource

import com.mongodb.client.MongoCollection
import data.dataSources.mongoDBDataSource.mongoDBParse.MongoDBParse
import data.repositories.dataSourceInterfaces.LogsDataSource
import data.repositories.dtoMappers.toLog
import data.repositories.dtoMappers.toLogDto
import logic.entities.Log
import logic.entities.Project
import logic.exceptions.ProjectNotFoundException
import logic.repositories.ProjectsRepository
import logic.repositories.TaskRepository
import logic.repositories.TaskStatesRepository
import org.bson.Document
import java.util.UUID

class MongoDBLogsDataSource(
    private val collection: MongoCollection<Document>,
    private val projectsRepository: ProjectsRepository,
    private val taskRepository: TaskRepository,
    private val taskStatesRepository: TaskStatesRepository,
    private val mongoParser: MongoDBParse
) : LogsDataSource {

    override fun getAllLogs(): List<Log> {
        return collection.find().map { doc ->
            mongoParser.documentToLogDto(doc).toLog()
        }.toList()
    }

    override fun addLog(log: Log) {
        val logDto = log.toLogDto()
        val doc = mongoParser.logDtoToDocument(logDto)
        collection.insertOne(doc)
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
        ///-----------
        val filter = Document("\$or", relatedEntityIds.map { id ->
            Document("loggedAction.entityId", id.toString())
        })

        return collection.find(filter)
            .map { mongoParser.documentToLogDto(it).toLog() }
            .sortedBy { it.time }
            .toList()
    }

}