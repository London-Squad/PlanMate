package ui

import logic.exceptions.*
import ui.cliPrintersAndReaders.CLIPrinter

class ViewExceptionHandler(private val cliPrinter: CLIPrinter) {
    fun <T> tryCall(anyFunction: () -> T): T {
        return try {
            anyFunction()
        } catch (e: NoLoggedInUserIsSavedInCacheException) {
            cliPrinter.cliPrintLn("Error: No user logged in. Please log in first.")
            throw e
        } catch (e: UserNotFoundException) {
            cliPrinter.cliPrintLn("Error: No user is logged in.")
            throw e
        } catch (e: UnauthorizedAccessException) {
            cliPrinter.cliPrintLn("Error: ${e.message}.")
            throw e
        } catch (e: InvalidUserNameLengthException) {
            cliPrinter.cliPrintLn("Error: Username should be at least 4 characters, alphanumeric, no whitespace.")
            throw e
        } catch (e: InvalidPasswordException) {
            cliPrinter.cliPrintLn("Error: ${e.message}.")
            throw e
        } catch (e: UsernameTakenException) {
            cliPrinter.cliPrintLn("Error: ${e.message}.")
            throw e
        } catch (e: RegistrationFailedException) {
            cliPrinter.cliPrintLn("Error: ${e.message}")
            throw e
        } catch (e: NotFoundException) {
            cliPrinter.cliPrintLn("Error: ${e.message ?: "Resource not found."}")
            throw e
        } catch (e: IndexOutOfBoundsException) {
            cliPrinter.cliPrintLn("Error: Selected index is out of bounds.")
            throw e
        } catch (e: AuthenticationException) {
            println(e.message)
            throw e
        } catch (e: Exception) {
            cliPrinter.cliPrintLn("Unexpected error: ${e.message}")
            throw e
        }
    }
}