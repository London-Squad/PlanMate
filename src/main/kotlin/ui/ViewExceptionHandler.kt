package ui

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import logic.exceptions.*
import ui.cliPrintersAndReaders.CLIPrinter

class ViewExceptionHandler(private val cliPrinter: CLIPrinter) {
    fun tryCall(anyFunction: suspend () -> Unit): Boolean {
        try {
            CoroutineScope(Dispatchers.IO).launch {
                anyFunction()
            }
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