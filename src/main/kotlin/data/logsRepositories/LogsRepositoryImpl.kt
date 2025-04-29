package logic.repositories


import data.logsRepositories.cvsFilesHandler.CsvReader
import data.logsRepositories.cvsFilesHandler.CsvWriter
import data.logsRepositories.cvsFilesHandler.FilePaths
import logic.entities.*
import java.time.format.DateTimeFormatter
import java.util.*

class LogsRepositoryImpl(
    private val reader: CsvReader = CsvReader(),
    private val writer: CsvWriter = CsvWriter()
) : LogsRepository {

    private val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd h:mm a")
    private val filePath = FilePaths.allLogsFile

    override fun getAllLogs(): List<Log> {
        return reader.readLines(filePath).mapNotNull { parseLogLine(it) }
    }

    override fun getLogById(id: UUID): List<Log> {
        return getAllLogs().filter { it.id == id }
    }

    override fun addLog(log: Log) {
        writer.appendLine(filePath, logToString(log))
    }

    private fun logToString(log: Log): String {
        val time = log.time.format(formatter)
        val user = log.user.userName
        val entity = log.action.let {
            when (it) {
                is Create -> "created ${getEntityType(it.entity)} (${it.entity.title})"
                is Delete -> "deleted ${getEntityType(it.entity)} (${it.entity.title})"
                is Edit -> "changed ${getEntityType(it.entity)} (${it.entity.title}) ${it.property} from ${it.oldValue} to ${it.newValue}"
            }
        }
        return "user $user $entity at $time"
    }

    private fun getEntityType(entity: PlanEntity): String {
        return when (entity) {
            is Project -> "project"
            is Task -> "task"
            is State -> "state"
            else -> "entity"
        }
    }

    private fun parseLogLine(line: String): Log? {

        return TODO("Provide the return value")
    }
}
