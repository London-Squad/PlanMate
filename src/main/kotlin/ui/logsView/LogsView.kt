package ui.logsView

import logic.entities.*
import logic.useCases.GetLogsByEntityIdUseCase
import logic.useCases.GetUsersUseCase
import ui.BaseView
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import ui.cliPrintersAndReaders.cliTable.CLITablePrinter
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
    fun printLogsByEntityId(entityId: UUID) {
        printLogs(entityId)
        cliReader.getUserInput("\npress enter to go back")
    }

    private fun printLogs(entityId: UUID) {
        var logs: List<Log> = emptyList()

        tryCall({
            logs = getLogsByEntityIdUseCase.getLogsByEntityId(entityId)
        }).also { success -> if (!success) return }

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
        var userName = "Unknown user"

        tryCall({
            userName = getUsersUseCase.getUsers()
                .firstOrNull { it.id == log.userId }
                ?.userName
                ?: userName
        })

        return userName
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