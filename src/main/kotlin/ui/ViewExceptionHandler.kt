package ui

import logic.exceptions.*
import ui.cliPrintersAndReaders.CLIPrinter

class ViewExceptionHandler(private val cliPrinter: CLIPrinter) {
    fun <T> tryCall(anyFunction: () -> T) {
        try {
            anyFunction()
        } catch (e: NoLoggedInUserIsSavedInCacheException) {
            cliPrinter.cliPrintLn("Error: No user logged in. Please log in first.")
        } catch (e: UserNotFoundException) {
            cliPrinter.cliPrintLn("Error: No user is logged in.")
        } catch (e: UnauthorizedAccessException) {
            cliPrinter.cliPrintLn("Error: ${e.message}.")
        } catch (e: InvalidUserNameLengthException) {
            cliPrinter.cliPrintLn("Error: Username should be at least 4 characters, alphanumeric, no whitespace.")
        } catch (e: InvalidPasswordException) {
            cliPrinter.cliPrintLn("Error: ${e.message}.")
        } catch (e: UsernameTakenException) {
            cliPrinter.cliPrintLn("Error: ${e.message}.")
        } catch (e: RegistrationFailedException) {
            cliPrinter.cliPrintLn("Error: ${e.message}")
        } catch (e: NotFoundException) {
            cliPrinter.cliPrintLn("Error: ${e.message ?: "Resource not found."}")
        } catch (e: IndexOutOfBoundsException) {
            cliPrinter.cliPrintLn("Error: Selected index is out of bounds.")
        } catch (e: AuthenticationException) {
            println(e.message)
        } catch (e: Exception) {
            cliPrinter.cliPrintLn("Unexpected error: ${e.message}")
        }
    }
}