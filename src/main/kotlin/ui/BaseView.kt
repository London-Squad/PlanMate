package ui

import logic.exceptions.*
import ui.cliPrintersAndReaders.CLIPrinter

open class BaseView(
    private val cliPrinter: CLIPrinter
) {
    fun tryCall(
        functionToTry: () -> Unit,
        onFailureFunction: (exception: Exception) -> Unit = (::handleDefaultExceptions)
    ): Boolean {
        return try {
            functionToTry()
            true
        } catch (exception: Exception) {
            onFailureFunction(exception)
            false
        }
    }

    fun handleDefaultExceptions(exception: Exception) {
        when (exception) {
            is AuthenticationException -> cliPrinter.cliPrintLn(exception.message ?: "something went wrong")
            is RetrievingDataFailureException -> cliPrinter.cliPrintLn(exception.message ?: "something went wrong")
            is StoringDataFailureException -> cliPrinter.cliPrintLn(exception.message ?: "something went wrong")
            else -> cliPrinter.cliPrintLn("Unexpected error: ${exception.message}")
        }
    }
}