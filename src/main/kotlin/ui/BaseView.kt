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
    private var caughtException: Exception? = null

    fun makeRequest(
        request: suspend CoroutineScope.() -> Unit,
        onSuccess: () -> Unit = {},
        onError: (exception: Exception) -> Unit = (::handleDefaultExceptions),
        // todo (extra): make onLoading,
        onLoadingMessage: String = DEFAULT_ON_LOADING_MESSAGE
    ) {
        sendRequestInSeparateScope(request)
        blockWithLoadingLoop(onLoadingMessage)
        processResult(onSuccess, onError)
    }

    private fun sendRequestInSeparateScope(operation: suspend CoroutineScope.() -> Unit) {
        isLoading = true
        caughtException = null

        CoroutineScope(Dispatchers.IO).launch {
            try {
                operation()
            } catch (exception: Exception) {
                caughtException = exception
            }
            stopLoading()
        }
    }

    private fun stopLoading() {
        isLoading = false
    }

    private fun blockWithLoadingLoop(onLoadingMessage: String) {
        var isLoadingMessagePrinted = false

        Thread.sleep(TIME_TO_SHOW_LOADING_MESSAGE_IN_MILLI_SECOND)
        if (isLoading) {
            cliPrinter.cliPrint(onLoadingMessage)
            isLoadingMessagePrinted = true
        }

        while (isLoading) {
            cliPrinter.cliPrint(".")
            Thread.sleep(TIME_INTERVAL_FOR_THE_LOADING_DOT_ANIMATION_IN_MILLI_SECONDS)
        }
        if (isLoadingMessagePrinted) cliPrinter.cliPrintLn("Done!")

    }

    private fun processResult(
        onSuccess: () -> Unit = {},
        onError: (exception: Exception) -> Unit = (::handleDefaultExceptions)
    ) {
        if (caughtException == null) onSuccess()
        else onError(caughtException!!)
    }

    fun handleDefaultExceptions(exception: Exception) {
        when (exception) {
            is AuthenticationException -> cliPrinter.cliPrintLn(exception.message ?: "something went wrong")
            is RetrievingDataFailureException -> cliPrinter.cliPrintLn(exception.message ?: "something went wrong")
            is StoringDataFailureException -> cliPrinter.cliPrintLn(exception.message ?: "something went wrong")
            else -> cliPrinter.cliPrintLn("Unexpected error: ${exception.message}")
        }
    }

    private companion object {
        const val TIME_TO_SHOW_LOADING_MESSAGE_IN_MILLI_SECOND = 100L
        const val TIME_INTERVAL_FOR_THE_LOADING_DOT_ANIMATION_IN_MILLI_SECONDS = 500L
        const val DEFAULT_ON_LOADING_MESSAGE = "Loading To Perform Your Request.."
    }
}