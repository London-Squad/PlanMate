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
            cliPrinter.cliPrintLn("Mate created successfully: ${user.userName}")
        } catch (e: AuthenticationException.InvalidUserNameLengthException) {
            cliPrinter.cliPrintLn("Error: Username must be 4-11 characters.")
        } catch (e: AuthenticationException.InvalidPasswordException) {
            cliPrinter.cliPrintLn("Error: Password must be 6-12 characters and contain at least one uppercase and one lowercase letter.")
        } catch (e: AuthenticationException.UsernameTakenException) {
            cliPrinter.cliPrintLn("Error: Username is already taken.")
        } catch (e: Exception) {
            cliPrinter.cliPrintLn("Unexpected error: ${e.message}")
        }
    }

}