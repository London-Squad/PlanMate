package ui.logsView

import logic.entities.*
import logic.useCases.GetLogsByEntityIdUseCase
import logic.useCases.GetUsersUseCase
import ui.RequestHandler
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import ui.cliPrintersAndReaders.CLITablePrinter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

class LogsView(
    private val cliReader: CLIReader,
    private val cliPrinter: CLIPrinter,
    private val getLogsByEntityIdUseCase: GetLogsByEntityIdUseCase,
    private val cliTablePrinter: CLITablePrinter,
    private val getUsersUseCase: GetUsersUseCase,
) : RequestHandler(cliPrinter) {

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
        return "user (${getUserNameByLog(log)}) ${actionToString(log)} at ${formatedTime(log.time)}"
    }

    private fun getUserNameByLog(log: Log): String {
        return users
            .firstOrNull { it.id == log.userId }
            ?.userName
            ?: "Unknown user"
    }

    private fun actionToString(log:Log): String {
        val entityType = log.entityType
        return when (val action = log.loggedAction) {
            is EntityCreationLog -> "created $entityType (${entityTitle(action.entityId)})"
            is EntityDeletionLog -> "deleted $entityType (${entityTitle(action.entityId)})"
            is EntityEditionLog -> "edited $entityType (${entityTitle(action.entityId)}) ${action.property} from (${action.oldValue}) to (${action.newValue}) "
        }
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