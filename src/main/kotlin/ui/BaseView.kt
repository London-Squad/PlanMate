package ui

import kotlinx.coroutines.*
import logic.exceptions.AuthenticationException
import logic.exceptions.RetrievingDataFailureException
import logic.exceptions.StoringDataFailureException
import ui.cliPrintersAndReaders.CLIPrinter

abstract class BaseView(
    private val cliPrinter: CLIPrinter
) {
    private val loadingScope = CoroutineScope(Dispatchers.Default)

    fun tryCall(
        functionToTry: suspend CoroutineScope.() -> Unit,
        onFailureFunction: (exception: Exception) -> Unit = { handleDefaultExceptions(it) }
    ): Boolean {

        startPrintingLoadingMessage()

        return try {

            runBlocking {
                functionToTry()
                stopPrintingLoadingMessage()
            }

            true
        } catch (exception: Exception) {
            stopPrintingLoadingMessage()
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

    private fun startPrintingLoadingMessage() {
        loadingScope.launch {
            delay(LOADING_MESSAGE_PRINT_INTERVAL)
            cliPrinter.cliPrint("Loading To Perform Your Request...")
            while (true) {
                delay(LOADING_MESSAGE_PRINT_INTERVAL)
                cliPrinter.cliPrint(".")
            }
        }
    }

    private fun stopPrintingLoadingMessage() {
        loadingScope.cancel()
        cliPrinter.cliPrintLn("")
    }

    private companion object {
        const val LOADING_MESSAGE_PRINT_INTERVAL = 10L
    }
}