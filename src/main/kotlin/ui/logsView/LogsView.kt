package ui.logsView

import logic.entities.*
import logic.useCases.GetLogsByEntityIdUseCase
import logic.useCases.GetUserByIdUseCase
import ui.ViewExceptionHandler
import ui.cliPrintersAndReaders.CLIReader
import ui.cliPrintersAndReaders.cliTable.CLITablePrinter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class LogsView(
    private val cliReader: CLIReader,
    private val getLogsByEntityIdUseCase: GetLogsByEntityIdUseCase,
    private val cliTablePrinter: CLITablePrinter,
    private val viewExceptionHandler: ViewExceptionHandler,
    private val getUserByIdUseCase: GetUserByIdUseCase,
) {
    fun printLogsByEntityId(entityId: UUID) {
        printLogs(entityId)
        cliReader.getUserInput("\npress enter to go back")

    }

    private fun printLogs(entityId: UUID) {
        viewExceptionHandler.tryCall {
            val logs = getLogsByEntityIdUseCase.getLogsByEntityId(entityId)
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
    }

    private fun buildLogMessage(log: Log): String {
        return "user (${getUserNameById(log.userId)}) ${actionToString(log.loggedAction)} at ${formatedTime(log.time)}"
    }

    private fun getUserNameById(useId: UUID): String? {
        var userName: String? = null
        viewExceptionHandler.tryCall {
            userName = getUserByIdUseCase(useId).userName
        }
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