package ui.logsView

import logic.entities.*
import logic.useCases.GetLogsByEntityIdUseCase
import logic.useCases.GetUsersUseCase
import ui.ViewExceptionHandler
import ui.ViewState
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
    private val getUsersUseCase: GetUsersUseCase,
) {
    private var currentViewState: ViewState<List<Log>> = ViewState.Loading

    suspend fun printLogsByEntityId(entityId: UUID, onComplete: suspend () -> Unit = {}) {
        loadAndDisplayLogs(entityId, onComplete)
    }

    private suspend fun loadAndDisplayLogs(entityId: UUID, onComplete: suspend () -> Unit) {
        viewExceptionHandler.executeWithState(
            onLoading = {
                currentViewState = ViewState.Loading
            },
            onSuccess = { logs ->
                currentViewState = ViewState.Success(logs)
                displayLogs(logs)
                cliReader.getUserInput("\npress enter to go back")
                onComplete()
            },
            onError = { exception ->
                currentViewState = ViewState.Error(exception)
                onComplete()
            },
            operation = {
                getLogsByEntityIdUseCase.getLogsByEntityId(entityId)
            }
        )
    }

    private suspend fun displayLogs(logs: List<Log>) {
        val headers = listOf("Log ID", "Action Message")
        val columnWidths = listOf(36, null)


        viewExceptionHandler.executeWithState(
            onLoading = {
            },
            onSuccess = { logMessages ->
                val data = logs.zip(logMessages).map { (log, message) ->
                    listOf(log.id.toString(), message)
                }
                cliTablePrinter(headers, data, columnWidths)
            },
            onError = { exception ->
                val data = logs.map { log ->
                    listOf(log.id.toString(), "Error retrieving log details")
                }
                cliTablePrinter(headers, data, columnWidths)
            },
            operation = {
                logs.map { log ->
                    buildLogMessage(log)
                }
            }
        )
    }

    private suspend fun buildLogMessage(log: Log): String {
        return "user (${getUserNameByLog(log)}) ${actionToString(log.loggedAction)} at ${formatedTime(log.time)}"
    }

    private suspend fun getUserNameByLog(log: Log): String {
        return getUsersUseCase.getUsers()
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