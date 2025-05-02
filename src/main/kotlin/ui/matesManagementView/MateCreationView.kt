package ui.matesManagementView

import logic.exception.AuthenticationException
import logic.usecases.CreateMateUseCase
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader

class MateCreationView(
    private val createMateUseCase: CreateMateUseCase,
    private val cliPrinter: CLIPrinter,
    private val cliReader: CLIReader
) {

    fun createMate() {
        cliPrinter.printHeader(" ============================= Create New Mate =============================")
        try {
            val username = cliReader.getUserInput("Enter username: ")
            val password = cliReader.getUserInput("Enter password: ")
            val user = createMateUseCase.createMate(username, password)
            cliPrinter.cliPrintLn("Mate created successfully: ${username}")
        } catch (e: AuthenticationException.UserNotFoundException) {
            cliPrinter.cliPrintLn("Error: No user is logged in.")
        } catch (e: AuthenticationException.UnauthorizedAccessException) {
            cliPrinter.cliPrintLn("Error: ${e.message}.")
        } catch (e: AuthenticationException.InvalidUserNameLengthException) {
            cliPrinter.cliPrintLn("Error: Username should be at least 4 characters, alphanumeric, no whitespace.")
        } catch (e: AuthenticationException.InvalidPasswordException) {
            cliPrinter.cliPrintLn("Error: ${e.message}.")
        } catch (e: AuthenticationException.UsernameTakenException) {
            cliPrinter.cliPrintLn("Error: ${e.message}.")
        } catch (e: AuthenticationException.RegistrationFailedException) {
            cliPrinter.cliPrintLn("Error: ${e.message}")
        } catch (e: Exception) {
            cliPrinter.cliPrintLn("Unexpected error: ${e.message}")
        }
    }

}