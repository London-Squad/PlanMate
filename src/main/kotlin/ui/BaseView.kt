package ui

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import logic.exceptions.AuthenticationException
import logic.exceptions.RetrievingDataFailureException
import logic.exceptions.StoringDataFailureException
import ui.cliPrintersAndReaders.CLIPrinter

abstract class BaseView(
    private val cliPrinter: CLIPrinter
) {
    private var isLoading = false
    private var exceptionToBeCaught: Exception? = null

    fun makeRequest(
        request: suspend CoroutineScope.() -> Unit,
        onSuccess: () -> Unit = {},
        onError: (exception: Exception) -> Unit = (::handleDefaultExceptions)
    ) {
        startOperationInSeparateScope(request)
        blockWithLoadingLoop()
        processResult(onSuccess, onError)
    }

    private fun startOperationInSeparateScope(operation: suspend CoroutineScope.() -> Unit) {
        isLoading = true
        exceptionToBeCaught = null

        CoroutineScope(Dispatchers.IO).launch {
            try {
                operation()
            } catch (exception: Exception) {
                exceptionToBeCaught = exception
            }
            stopLoading()
        }
    }

    private fun processResult(
        onSuccess: () -> Unit = {},
        onError: (exception: Exception) -> Unit = (::handleDefaultExceptions)
    ) {
        if (exceptionToBeCaught == null) onSuccess()
        else onError(exceptionToBeCaught!!)
    }

    fun handleDefaultExceptions(exception: Exception) {
        when (exception) {
            is AuthenticationException -> cliPrinter.cliPrintLn(exception.message ?: "something went wrong")
            is RetrievingDataFailureException -> cliPrinter.cliPrintLn(exception.message ?: "something went wrong")
            is StoringDataFailureException -> cliPrinter.cliPrintLn(exception.message ?: "something went wrong")
            else -> cliPrinter.cliPrintLn("Unexpected error: ${exception.message}")
        }
    }

    private fun blockWithLoadingLoop() {
        Thread.sleep(LOADING_MESSAGE_PRINT_INTERVAL)
        if (isLoading) cliPrinter.cliPrint("Loading To Perform Your Request..")

        while (isLoading) {
            cliPrinter.cliPrint(".")
            Thread.sleep(LOADING_MESSAGE_PRINT_INTERVAL)
        }
    }

    private fun stopLoading() {
        isLoading = false
        cliPrinter.cliPrintLn("")
    }

    private companion object {
        const val LOADING_MESSAGE_PRINT_INTERVAL = 500L
    }
}