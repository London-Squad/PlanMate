package ui

import kotlinx.coroutines.runBlocking
import logic.exceptions.*
import ui.cliPrintersAndReaders.CLIPrinter

class ViewExceptionHandler(private val cliPrinter: CLIPrinter) {
    fun tryCall(anyFunction: suspend () -> Unit): Boolean {
        return try {
            runBlocking {
                anyFunction()
            }
            true
        } catch (e: AuthenticationException) {
            printLn(e.message ?: "something went wrong")
            false
        } catch (e: RetrievingDataFailureException) {
            printLn(e.message ?: "something went wrong")
            false
        } catch (e: StoringDataFailureException) {
            printLn(e.message ?: "something went wrong")
            false
        } catch (e: Exception) {
            printLn("Unexpected error: ${e.message}")
            false
        }
    }

    private fun printLn(message: String) = cliPrinter.cliPrintLn(message)
}