package data.dataSources.mongoDBDataSource

import com.mongodb.client.MongoCollection
import data.dataSources.mongoDBDataSource.mongoDBParse.MongoDBParse
import data.dto.LogDto
import data.exceptions.DataAccessException
import data.exceptions.DataParsingException
import data.exceptions.handleException
import data.exceptions.sharedOperationTypes.MongoOperationName
import data.repositories.dataSourceInterfaces.LogsDataSource
import org.bson.Document

class MongoDBLogsDataSource(
    private val collection: MongoCollection<Document>, private val mongoParser: MongoDBParse
) : LogsDataSource {

    override fun getAllLogs(): List<LogDto> {
        return handleException(MongoOperationName.RETRIEVE_PROJECTS) {
            collection.find().map { doc ->
                try {
                    mongoParser.documentToLogDto(doc)
                } catch (e: DataAccessException) {
                    throw DataParsingException("Failed to parse document: ${e.message}")
                }
            }.toList()
        }
    }

    override fun addLog(logDto: LogDto) {
        val doc = mongoParser.logDtoToDocument(logDto)
        collection.insertOne(doc)
    }
}