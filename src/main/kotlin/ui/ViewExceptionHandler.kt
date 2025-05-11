package ui

import kotlinx.coroutines.*
import logic.exceptions.*
import ui.cliPrintersAndReaders.CLIPrinter

class ViewExceptionHandler(
    private val cliPrinter: CLIPrinter
) {
    suspend fun <T> executeWithState(
        onLoading: () -> Unit,
        onSuccess: suspend (T) -> Unit,
        onError: suspend (Exception) -> Unit,
        operation: suspend () -> T
    ) {
        CoroutineScope(Dispatchers.Default).launch {
            onLoading()
        }
        try {
            val result = withContext(Dispatchers.IO) {
                operation()
            }
            withContext(Dispatchers.Default) {
                onSuccess(result)
            }
        } catch (e: AuthenticationException) {
            withContext(Dispatchers.Default) {
                printLn(e.message ?: "Authentication error")
                onError(e)
            }
        } catch (e: RetrievingDataFailureException) {
            withContext(Dispatchers.Default) {
                printLn(e.message ?: "Failed to retrieve data")
                onError(e)
            }
        } catch (e: StoringDataFailureException) {
            withContext(Dispatchers.Default) {
                printLn(e.message ?: "Failed to store data")
                onError(e)
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Default) {
                printLn("Unexpected error: ${e.message}")
                onError(e)
            }
        }
    }

    private fun printLn(message: String) = cliPrinter.cliPrintLn(message)
}

sealed class ViewState<out T> {
    object Loading : ViewState<Nothing>()
    data class Success<T>(val data: T) : ViewState<T>()
    data class Error(val exception: Exception) : ViewState<Nothing>()
}