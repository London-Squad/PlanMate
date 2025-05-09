package data.dataSources.mongoDBDataSource

import com.mongodb.client.MongoCollection
import data.dataSources.mongoDBDataSource.mongoDBParse.MongoDBParse
import data.repositories.dataSourceInterfaces.LogsDataSource
import data.dto.LogDto
import data.exceptions.*
import org.bson.Document

class MongoDBLogsDataSource(
    private val collection: MongoCollection<Document>,
    private val mongoParser: MongoDBParse
) : LogsDataSource {

    override fun getAllLogs(): List<LogDto> {
        return try{
            collection.find().map { doc ->
                try {
                    mongoParser.documentToLogDto(doc)
                } catch (e: DataAccessException) {
                    throw DataParsingException("Failed to parse document: ${e.message}")
                }
            }.toList()
        }catch(e: DataAccessException){
            throw DataConnectionException("Unexpected error while retrieving logs: ${e.message}")

        }
    }

    override fun addLog(logDto: LogDto) {
        val doc = mongoParser.logDtoToDocument(logDto)
        collection.insertOne(doc)
    }
}