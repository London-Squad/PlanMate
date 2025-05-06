package ui

import data.exceptions.NoLoggedInUserException
import logic.exceptions.*
import logic.exceptions.authenticationExceptions.AuthenticationException
import logic.exceptions.authenticationExceptions.InvalidPasswordException
import logic.exceptions.authenticationExceptions.InvalidUserNameLengthException
import logic.exceptions.authenticationExceptions.RegistrationFailedException
import logic.exceptions.authenticationExceptions.UnauthorizedAccessException
import logic.exceptions.authenticationExceptions.UseNameAlreadyExistException
import logic.exceptions.notFoundExecption.NotFoundException
import logic.exceptions.notFoundExecption.UserNotFoundException
import ui.cliPrintersAndReaders.CLIPrinter

class ViewExceptionHandler(private val cliPrinter: CLIPrinter) {
    fun  tryCall(anyFunction: () -> Unit) {
        try {
            anyFunction()
        } catch (e: NoLoggedInUserException) {
            cliPrinter.cliPrintLn("Error: No user logged in. Please log in first.")
        } catch (e: UserNotFoundException) {
            cliPrinter.cliPrintLn("Error: No user is logged in.")
        } catch (e: IndexOutOfBoundsException) {
            cliPrinter.cliPrintLn("Error: Selected index is out of bounds.")
        } catch (e: NotFoundException) {
            cliPrinter.cliPrintLn("Unexpected error: ${e.message}")
        } catch (e: InvalidUserNameLengthException) {
            cliPrinter.cliPrintLn("Unexpected error: ${e.message}")
        } catch (e: AuthenticationException) {
            cliPrinter.cliPrintLn("Unexpected error: ${e.message}")
        } catch (e: InvalidPasswordException) {
            cliPrinter.cliPrintLn("Unexpected error: ${e.message}")
        } catch (e: UseNameAlreadyExistException) {
            cliPrinter.cliPrintLn("Unexpected error: ${e.message}")
        } catch (e: UnauthorizedAccessException) {
            cliPrinter.cliPrintLn("Unexpected error: ${e.message}")
        } catch (e: RegistrationFailedException) {
            cliPrinter.cliPrintLn("Unexpected error: ${e.message}")
        } catch (e: Exception) {
            cliPrinter.cliPrintLn("Unexpected error: ${e.message}")
        }
    }
}