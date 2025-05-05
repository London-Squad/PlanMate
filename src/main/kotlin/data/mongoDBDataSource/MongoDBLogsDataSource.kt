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
}