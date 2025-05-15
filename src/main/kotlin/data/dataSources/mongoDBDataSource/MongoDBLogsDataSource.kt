package data.dataSources.mongoDBDataSource

import com.mongodb.client.model.Filters
import data.dataSources.mongoDBDataSource.mongoDBHandler.MongoDBParser
import data.dataSources.mongoDBDataSource.mongoDBHandler.MongoDBQueryHandler
import data.repositories.dtoMappers.toLog
import data.repositories.dtoMappers.toLogDto
import logic.entities.Log
import logic.repositories.LogsRepository

class MongoDBLogsDataSource(
    private val logQueryHandler: MongoDBQueryHandler,
    private val mongoParser: MongoDBParser
) : LogsRepository {

    override suspend fun getAllLogs(): List<Log> {
        val filters = Filters.empty()
        return logQueryHandler.fetchManyFromCollection(filters).map { doc ->
            mongoParser.documentToLogDto(doc).toLog()
        }
    }

    override suspend fun addLog(log: Log) {
        log.toLogDto().let(mongoParser::logDtoToDocument).also { logQueryHandler.insertToCollection(it) }
    }

    override suspend fun getLogsByEntityId(entityId: MutableSet<String>): List<Log> {
        val filter = Filters.eq(MongoDBParser.PLAN_ENTITY_ID_FIELD, entityId.toString())
        return logQueryHandler.fetchManyFromCollection(filter).map { mongoParser.documentToLogDto(it).toLog() }
    }
}