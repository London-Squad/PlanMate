package ui.logsView

import logic.entities.*
import logic.useCases.GetLogsByEntityIdUseCase
import logic.useCases.GetUsersUseCase
import ui.BaseView
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import ui.cliPrintersAndReaders.CLITablePrinter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class LogsView(
    private val cliReader: CLIReader,
    private val cliPrinter: CLIPrinter,
    private val getLogsByEntityIdUseCase: GetLogsByEntityIdUseCase,
    private val cliTablePrinter: CLITablePrinter,
    private val getUsersUseCase: GetUsersUseCase,
) : BaseView(cliPrinter) {

    private lateinit var users: List<User>

    fun printLogsByEntityId(entityId: UUID) {
        var logs: List<Log> = emptyList()

        makeRequest(
            request = { logs = getLogsByEntityIdUseCase.getLogsByEntityId(entityId) },
            onSuccess = { printLogs(logs) },
            onLoadingMessage = "Fetching logs..."
        )

        cliReader.getUserInput("\npress enter to go back")
    }

    private fun printLogs(logs: List<Log>) {
        users = emptyList()
        makeRequest(
            request = { users = getUsersUseCase.getUsers() },
            onLoadingMessage = "Fetching users for the logs..."
        )

        if (logs.isEmpty()) {
            cliPrinter.cliPrintLn("No logs found for this entity.")
            return
        }

        val headers = listOf("Log ID", "Action Message")
        val data = logs.map { log ->
            listOf(
                log.id.toString(),
                buildLogMessage(log)
            )
        }
        val columnWidths = listOf(36, null)
        cliTablePrinter(headers, data, columnWidths)
    }

    private fun buildLogMessage(log: Log): String {
        return "user (${getUserNameByLog(log)}) ${actionToString(log.loggedAction)} at ${formatedTime(log.time)}"
    }

    private fun getUserNameByLog(log: Log): String {
        return users
            .firstOrNull { it.id == log.userId }
            ?.userName
            ?: "Unknown user"
    }

    private fun actionToString(action: LoggedAction): String {
        return when (action) {
            is EntityCreationLog -> "created ${entityType(action.entityId)} (${entityTitle(action.entityId)})"
            is EntityDeletionLog -> "deleted ${entityType(action.entityId)} (${entityTitle(action.entityId)})"
            is EntityEditionLog -> "edited ${entityType(action.entityId)} (${entityTitle(action.entityId)}) ${action.property} from (${action.oldValue}) to (${action.newValue}) "
        }
    }

    private fun entityType(entityId: UUID): String {
        // todo: implement this function to return the type of the entity based on its ID
        return "Entity Type"
    }

    private fun entityTitle(entityId: UUID): String {
        // todo: implement this function to return the title of the entity based on its ID
        return "Entity Title"
    }

    private fun formatedTime(time: LocalDateTime): String {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
        return time.format(formatter)
    }
}