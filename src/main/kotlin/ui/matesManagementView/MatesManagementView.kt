package ui.matesManagementView

import logic.entities.User
import logic.useCases.mateUseCase.GetAllMatesUseCase
import logic.useCases.GetLoggedInUserUseCase
import ui.ViewExceptionHandler
import ui.ViewState
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import ui.cliPrintersAndReaders.cliTable.CLITablePrinter

class MatesManagementView(
    private val cliPrinter: CLIPrinter,
    private val cliReader: CLIReader,
    private val getLoggedInUserUseCase: GetLoggedInUserUseCase,
    private val getAllMatesUseCase: GetAllMatesUseCase,
    private val cliTablePrinter: CLITablePrinter,
    private val mateCreationView: MateCreationView,
    private val viewExceptionHandler: ViewExceptionHandler
) {
    private var currentState: ViewState<List<User>> = ViewState.Loading

    suspend fun start() {
        loadLoggedInUser()
    }

    private suspend fun loadLoggedInUser() {
        viewExceptionHandler.executeWithState(
            onLoading = {
                printLn("Loading user information...")
                currentState = ViewState.Loading
            },
            onSuccess = { currentUser ->
                if (currentUser.type != User.Type.ADMIN) {
                    printLn("Error: Only admins can manage mates.")
                    return@executeWithState
                }
                loadMates()
            },
            onError = { exception ->
                printLn("Failed to verify user permissions: ${exception.message}")
            },
            operation = {
                getLoggedInUserUseCase.getLoggedInUser()
            }
        )
    }

    private suspend fun loadMates() {
        viewExceptionHandler.executeWithState(
            onLoading = {
                printLn("Loading mates...")
                currentState = ViewState.Loading
            },
            onSuccess = { mates ->
                currentState = ViewState.Success(mates)
                displayMatesManagementUI(mates)
            },
            onError = { exception ->
                currentState = ViewState.Error(exception)
                printLn("Failed to load mates: ${exception.message}")
            },
            operation = {
                getAllMatesUseCase.getAllMates()
            }
        )
    }

    private suspend fun displayMatesManagementUI(mates: List<User>) {
        displayMatesList(mates)
        printOptions()
        selectNextUI()
    }

    private fun displayMatesList(mates: List<User>) {
        if (mates.isEmpty()) {
            printLn("No mates found.")
            return
        }

        val headers = listOf("ID", "Username", "Type")
        val data = mates.map { mate ->
            listOf(mate.id.toString(), mate.userName, mate.type.toString())
        }
        val columnWidths = listOf(null, null, null)

        cliTablePrinter(headers, data, columnWidths)
    }

    private fun printOptions() {
        printLn("Mates Management Menu")
        printLn("1. Create New Mate")
        printLn("0. Back")
    }

    private suspend fun selectNextUI() {
        val userInput = cliReader.getValidInputNumberInRange(
            min = 0,
            max = MAX_OPTION_NUMBER
        )

        when (userInput) {
            1 -> createMate()
            0 -> return
        }
    }

    private suspend fun createMate() {
        mateCreationView.createMate {
            start()
        }
    }

    private fun printLn(message: String) {
        cliPrinter.cliPrintLn(message)
    }

    private companion object {
        const val MAX_OPTION_NUMBER = 1
    }
}