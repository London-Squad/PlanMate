package data.dataSources.mongoDBDataSource

import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.MongoException
import data.dataSources.mongoDBDataSource.mongoDBParse.MongoDBParse
import data.dto.LogDto
import kotlinx.coroutines.flow.map
import data.repositories.dataSourceInterfaces.LogsDataSource
import logic.exceptions.RetrievingDataFailureException
import logic.exceptions.StoringDataFailureException
import org.bson.Document
import kotlinx.coroutines.flow.toList

class MongoDBLogsDataSource(
    private val collection: MongoCollection<Document>,
    private val mongoParser: MongoDBParse
) : LogsDataSource {
    override suspend fun getAllLogs(): List<LogDto> {
        try {
            return collection.find().map { doc ->
                mongoParser.documentToLogDto(doc)
            }.toList()
        } catch (e: MongoException) {
            throw RetrievingDataFailureException("Failed to retrieve logs: ${e.message}")
        }
    }

    override suspend fun addLog(logDto: LogDto) {
        try {
            val doc = mongoParser.logDtoToDocument(logDto)
            collection.insertOne(doc)
        } catch (e: MongoException) {
            throw StoringDataFailureException("Failed to add log: ${e.message}")
        }
    }
}