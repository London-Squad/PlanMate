package ui

import logic.planeMateException.*
import ui.cliPrintersAndReaders.CLIPrinter

class ViewExceptionHandler(private val cliPrinter: CLIPrinter) {
    fun tryCall(anyFunction: () -> Unit): Boolean {
        try {
            anyFunction()
            return true
        } catch (e: AuthenticationException) {
            printLn(e.message ?: "something went wrong")
        } catch (e: RetrievingDataFailureException) {
            printLn(e.message ?: "something went wrong")
        } catch (e: StoringDataFailureException) {
            printLn(e.message ?: "something went wrong")
        } catch (e: Exception) {
            printLn("Unexpected error: ${e.message}")
        }
        return false
    }

    private fun printLn(message: String) = cliPrinter.cliPrintLn(message)
}