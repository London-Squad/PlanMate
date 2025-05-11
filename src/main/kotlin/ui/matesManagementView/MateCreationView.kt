package ui.matesManagementView

import logic.useCases.mateUseCase.CreateMateUseCase
import ui.BaseView
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader

class MateCreationView(
    private val createMateUseCase: CreateMateUseCase,
    private val cliPrinter: CLIPrinter,
    private val cliReader: CLIReader,
    private val baseView: BaseView
) {
    fun createMate() {
        cliPrinter.printHeader("Create New Mate")
        baseView.tryCall {
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
        }
    }

}