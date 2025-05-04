package ui.logsView

import logic.entities.*
import logic.useCases.GetLogsByEntityIdUseCase
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class LogsView(
    private val cliPrinter: CLIPrinter,
    private val cliReader: CLIReader,
    private val getLogsByEntityIdUseCase: GetLogsByEntityIdUseCase
) {
    fun printLogsByEntityId(entityId: UUID) {

        printHeader()
        printLogsTableHeader()
        printLogs(entityId)
        cliReader.getUserInput("\npress enter to go back")

    }

    private fun printHeader() {
        cliPrinter.printHeader("Logs")
    }

    private fun printLogsTableHeader() {
        printLn("----------------------------------------------------------------------------------------------------------------------------------------------")
        printLn("Log Id                               | time                | log message                                                                      ")
        printLn("----------------------------------------------------------------------------------------------------------------------------------------------")
    }

    private fun printLogs(entityId: UUID) {
        getLogsByEntityIdUseCase.getLogsByEntityId(entityId).forEach(::printLog)
    }

    private fun printLog(log: Log) {
        printLn("${log.id} | ${formatedTime(log.time)} | ${log.message} ")
    }

    private fun formatedTime(time: LocalDateTime): String {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
        return time.format(formatter)
    }

    private fun printLn(message: String) = cliPrinter.cliPrintLn(message)
}