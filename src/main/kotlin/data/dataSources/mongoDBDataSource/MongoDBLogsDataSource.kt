package data.dataSources.mongoDBDataSource

import com.mongodb.MongoException
import com.mongodb.client.MongoCollection
import data.dataSources.mongoDBDataSource.mongoDBParse.MongoDBParse
import data.dto.LogDto
import data.repositories.dataSourceInterfaces.LogsDataSource
import logic.planeMateException.RetrievingDataFailureException
import logic.planeMateException.StoringDataFailureException
import org.bson.Document

class MongoDBLogsDataSource(
    private val collection: MongoCollection<Document>, private val mongoParser: MongoDBParse
) : LogsDataSource {
    override fun getAllLogs(): List<LogDto> {
        try {
            return collection.find().map { doc ->
                mongoParser.documentToLogDto(doc)
            }.toList()
        } catch (e: MongoException) {
            throw RetrievingDataFailureException("Failed to retrieve logs: ${e.message}")
        }
    }

    override fun addLog(logDto: LogDto) {
        try {
            val doc = mongoParser.logDtoToDocument(logDto)
            collection.insertOne(doc)
        } catch (e: MongoException) {
            throw StoringDataFailureException("Failed to add log: ${e.message}")
        }
    }
}