package data.dataSources.mongoDBDataSource

import com.mongodb.client.MongoCollection
import data.dataSources.mongoDBDataSource.mongoDBParse.MongoDBParse
import data.repositories.LogsDataSource
import data.dto.LogDto
import org.bson.Document

class MongoDBLogsDataSource(
    private val collection: MongoCollection<Document>,
    private val mongoParser: MongoDBParse
) : LogsDataSource {

    override fun getAllLogs(): List<LogDto> {
        return collection.find().map { doc ->
            mongoParser.documentToLogDto(doc)
        }.toList()
    }

    override fun addLog(logDto: LogDto) {
        val doc = mongoParser.logDtoToDocument(logDto)
        collection.insertOne(doc)
    }
}