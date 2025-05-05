package ui.logsView

import logic.entities.*
import logic.useCases.GetLogsByEntityIdUseCase
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import ui.cliPrintersAndReaders.cliTable.CLITablePrinter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class LogsView(
    private val cliReader: CLIReader,
    private val getLogsByEntityIdUseCase: GetLogsByEntityIdUseCase,
    private val cliTablePrinter: CLITablePrinter
) {
    fun printLogsByEntityId(entityId: UUID) {
        printLogs(entityId)
        cliReader.getUserInput("\nPress enter to go back")

    }

    private fun printLogs(entityId: UUID) {
        val logs = getLogsByEntityIdUseCase.getLogsByEntityId(entityId)

        val headers = listOf("Log ID", "Action Message")
        val data = logs.map { log ->
            listOf(
                log.id.toString(),
                "user (${log.user.userName}) ${actionToString(log.action)} at ${formatedTime(log.time)}"
            )
        }

        val columnWidths = listOf(36, null)
        cliTablePrinter(headers, data, columnWidths)
    }


    private fun actionToString(action: Action): String {
        return when (action) {
            is Create -> "created ${entityName(action.entity)} (${action.entity.title})"
            is Delete -> "deleted ${entityName(action.entity)} (${action.entity.title})"
            is Edit -> "edited ${entityName(action.entity)} (${action.entity.title}) ${action.property} from (${action.oldValue}) to (${action.newValue}) "
        }
    }

    private fun entityName(entity: PlanEntity): String {
        return when (entity) {
            is Project -> "project"
            is State -> "state"
            is Task -> "task"
            else -> "unknown entity"
        }
    }

    private fun formatedTime(time: LocalDateTime): String {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
        return time.format(formatter)
    }

}