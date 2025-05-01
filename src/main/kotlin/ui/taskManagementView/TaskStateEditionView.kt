package ui.taskManagementView

import logic.entities.State
import logic.entities.Task
import logic.useCases.ManageTaskUseCase
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader

class TaskStateEditionView(
    private val cliReader: CLIReader,
    private val cliPrinter: CLIPrinter,
    private val manageTaskUseCase: ManageTaskUseCase
) {

    fun editState(task: Task, projectStates: List<State>) {

        if (projectStates.isEmpty()) {
            printLn("no states available")
            return
        }

        printProjectState(projectStates)
        val newState = getValidState(projectStates)

        manageTaskUseCase.editTaskState(task.id, newState.id)
    }

    private fun printProjectState(states: List<State>) {
        states.forEachIndexed { index, state ->
            printLn("${index + 1}. ${state.title}")
        }
    }

    private fun getValidState(states: List<State>): State {
        val userInput = cliReader.getUserInput("Your choice: ").toIntOrNull()
        if (userInput in 1..states.size)
            return states[userInput!! - 1]
        else {
            printLn("Invalid input")
            return getValidState(states)
        }
    }

    private fun printLn(message: String) {
        cliPrinter.cliPrintLn(message)
    }
}