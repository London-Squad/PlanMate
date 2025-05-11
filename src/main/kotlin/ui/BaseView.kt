package ui

import logic.exceptions.*
import ui.cliPrintersAndReaders.CLIPrinter

abstract class BaseView(
    private val cliPrinter: CLIPrinter
) {
    fun tryCall(
        functionToTry: () -> Unit,
        onFailureFunction: (exception: Exception) -> Unit = (::handleExceptionsInDefaultWay)
    ) {
        try {
            functionToTry()
        } catch (exception: Exception) {
            onFailureFunction(exception)
        }
    }

    fun handleExceptionsInDefaultWay(exception: Exception) {
        when (exception) {
            is AuthenticationException -> cliPrinter.cliPrintLn(exception.message ?: "something went wrong")
            is RetrievingDataFailureException -> cliPrinter.cliPrintLn(exception.message ?: "something went wrong")
            is StoringDataFailureException -> cliPrinter.cliPrintLn(exception.message ?: "something went wrong")
            else -> cliPrinter.cliPrintLn("Unexpected error: ${exception.message}")
        }
    }
}