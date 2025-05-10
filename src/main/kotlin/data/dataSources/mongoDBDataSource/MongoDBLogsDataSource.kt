package data.dataSources.mongoDBDataSource

import com.mongodb.kotlin.client.coroutine.MongoCollection
import data.dataSources.mongoDBDataSource.mongoDBParse.MongoDBParse
import data.repositories.dataSourceInterfaces.LogsDataSource
import data.dto.LogDto
import kotlinx.coroutines.flow.map
import org.bson.Document
import kotlinx.coroutines.flow.toList

class MongoDBLogsDataSource(
    private val collection: MongoCollection<Document>,
    private val mongoParser: MongoDBParse
) : LogsDataSource {

    override suspend fun getAllLogs(): List<LogDto> {
        return collection.find()
            .map { doc -> mongoParser.documentToLogDto(doc) }
            .toList()
    }

    override suspend fun addLog(logDto: LogDto) {
        val doc = mongoParser.logDtoToDocument(logDto)
        collection.insertOne(doc).let { }
    }
}