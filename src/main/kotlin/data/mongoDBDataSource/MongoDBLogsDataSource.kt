package data.mongoDBDataSource

import logic.entities.Log
import logic.repositories.LogsRepository
import java.util.*

class MongoDBLogsDataSource() : LogsRepository {
    override fun getAllLogs(): List<Log> {
        TODO("Not yet implemented")
    }

    override fun getLogsByEntityId(entityId: UUID): List<Log> {
        TODO("Not yet implemented")
    }

    override fun addLog(log: Log) {
        TODO("Not yet implemented")
    }

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
}
