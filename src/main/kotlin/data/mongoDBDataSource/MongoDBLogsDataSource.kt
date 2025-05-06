package data.mongoDBDataSource

import com.mongodb.client.MongoCollection
import data.dataSources.LogsDataSource
import data.dto.LogDto
import org.bson.Document
import java.time.LocalDateTime
import java.util.UUID

class MongoDBLogsDataSource(
    private val collection: MongoCollection<Document> = DatabaseConnection.getUsersCollection()
) : LogsDataSource {

    companion object {
        private const val ID_FIELD = "id"
        private const val USER_ID_FIELD = "userId"
        private const val TIME_FIELD = "time"
        private const val ACTION_FIELD = "action"
        private const val PLAN_ENTITY_ID_FIELD = "planEntityId"
        private const val PLAN_ENTITY_PROPERTY_FIELD = "planEntityProperty"
        private const val PLAN_OLD_VALUE_FIELD = "oldValue"
        private const val PLAN_NEW_VALUE_FIELD = "newValue"
    }

    override fun getAllLogs(): List<LogDto> {
        var result: List<LogDto> = emptyList()
            result = collection.find().map { doc ->
                LogDto(
                    id = UUID.fromString(doc.getString(ID_FIELD)),
                    userId = UUID.fromString(doc.getString(USER_ID_FIELD)),
                    time = LocalDateTime.parse(doc.getString(TIME_FIELD)),
                    action = doc.getString(ACTION_FIELD),
                    planEntityId = UUID.fromString(doc.getString(PLAN_ENTITY_ID_FIELD)),
                    planEntityProperty = doc.getString(PLAN_ENTITY_PROPERTY_FIELD),
                    oldValue = doc.getString(PLAN_OLD_VALUE_FIELD),
                    newValue = doc.getString(PLAN_NEW_VALUE_FIELD)
                )
            }.toList()
        return result
    }

    override fun addLog(logDto: LogDto) {
            val doc = Document(ID_FIELD, logDto.id.toString())
                .append(USER_ID_FIELD, logDto.userId.toString())
                .append(TIME_FIELD, logDto.time.toString())
                .append(ACTION_FIELD, logDto.action)
                .append(PLAN_ENTITY_ID_FIELD, logDto.planEntityId.toString())
                .append(PLAN_ENTITY_PROPERTY_FIELD, logDto.planEntityProperty)
                .append(PLAN_OLD_VALUE_FIELD, logDto.oldValue)
                .append(PLAN_NEW_VALUE_FIELD, logDto.newValue)
            collection.insertOne(doc)
    }
}