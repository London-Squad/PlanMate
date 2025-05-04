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
        val newStateIndex = cliReader.getValidUserNumberInRange(projectStates.size - 1).toInt()

        manageTaskUseCase.editTaskState(task.id, projectStates[newStateIndex])
    }

    private fun printProjectState(states: List<State>) {
        states.forEachIndexed { index, state ->
            printLn("${index + 1}. ${state.title}")
        }
    }

    private fun printLn(message: String) {
        cliPrinter.cliPrintLn(message)
    }
}