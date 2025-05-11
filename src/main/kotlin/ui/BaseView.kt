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
    private var loading = false

    fun tryCall(
        functionToTry: suspend CoroutineScope.() -> Unit,
        onFailureFunction: (exception: Exception) -> Unit = { handleDefaultExceptions(it) }
    ): Boolean {

        var success = false

        CoroutineScope(Dispatchers.Default).launch {
            try {
                functionToTry()
                success = true
                stopLoading()
            } catch (exception: Exception) {
                stopLoading()
                onFailureFunction(exception)
            }
        }

        startLoading()

        return success
    }

    fun handleDefaultExceptions(exception: Exception) {
        when (exception) {
            is AuthenticationException -> cliPrinter.cliPrintLn(exception.message ?: "something went wrong")
            is RetrievingDataFailureException -> cliPrinter.cliPrintLn(exception.message ?: "something went wrong")
            is StoringDataFailureException -> cliPrinter.cliPrintLn(exception.message ?: "something went wrong")
            else -> cliPrinter.cliPrintLn("Unexpected error: ${exception.message}")
        }
    }

    private fun startLoading() {
        loading = true
        Thread.sleep(LOADING_MESSAGE_PRINT_INTERVAL)
        if (loading) cliPrinter.cliPrint("Loading To Perform Your Request...")
        while (loading) {
            cliPrinter.cliPrint(".")
            Thread.sleep(LOADING_MESSAGE_PRINT_INTERVAL)
        }
    }

    private fun stopLoading() {
        loading = false
        cliPrinter.cliPrintLn("")
    }

    private companion object {
        const val LOADING_MESSAGE_PRINT_INTERVAL = 50L
    }
}