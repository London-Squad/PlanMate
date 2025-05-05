package ui.matesManagementView

import logic.exceptions.*
import logic.useCases.CreateMateUseCase
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
            val username = cliReader.getValidUserInput(
                { it.isNotBlank() },
                "Enter username: ",
                "username cant be empty"
            )
            val password = cliReader.getValidUserInput(
                { it.isNotBlank() },
                "Enter password: ",
                "password cant be empty"
            )

            createMateUseCase.createMate(username, password)
            cliPrinter.cliPrintLn("Mate created successfully: $username")
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
        } catch (e: Exception) {
            cliPrinter.cliPrintLn("Unexpected error: ${e.message}")
        }
    }

}