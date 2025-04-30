package ui.taskManagementView

import data.catchData.CatchDataMemoryRepository
import logic.entities.State
import logic.entities.Task
import logic.useCases.ManageTaskUseCase
import ui.CLIPrintersAndReaders.CLIPrinter
import ui.CLIPrintersAndReaders.CLIReader
import ui.View

class TaskManagementView(
    private val manageTaskUseCase: ManageTaskUseCase,
    private val catchDataMemoryRepository: CatchDataMemoryRepository,
    private val cliPrinter: CLIPrinter,
    private val cliReader: CLIReader,
) : View {

    override fun start() {

        val currentTask = catchDataMemoryRepository.getSelectedTask() ?: run {
            cliPrinter.cliPrintLn("please select task first")

            return
        }

        printTask(currentTask)
        printOptions()

        selectNextUI(currentTask)
    }


    private fun printTask(task: Task) {
        printLn("Task: ${task.title}")
        printLn("Description: ${task.description}")
    }

    private fun printOptions() {
        printLn("1. Edit Title")
        printLn("2. Edit description")
        printLn("3. Edit state")
        printLn("4. delete task")
        printLn("0. back")
    }

    private fun selectNextUI(task: Task) {
        when (getValidUserInput(OPTIONS_LIST).toIntOrNull()) {
            1 -> editTitle(task)
            2 -> editDescription(task)
            3 -> editState(task)
            4 -> deleteTask(task)
            0 -> return
            else -> {
                printLn("Invalid option")
            }
        }
    }

    private fun getValidUserInput(options: List<String>): String {
        val userInput = cliReader.getUserInput("your choice:")
        if (userInput in options) return userInput
        else {
            printLn("Invalid option")
            return getValidUserInput(options)
        }
    }

    private fun editTitle(task: Task) {
        val newTitle = getValidTitle()
        manageTaskUseCase.editTaskTitle(task.id, newTitle)
        start()
    }

    private fun getValidTitle(): String {
        val userInput = cliReader.getUserInput("New Title: ")
        if (userInput.isNotBlank() && userInput.length <= MAX_TITLE_LENGTH) return userInput
        else {
            printLn("Invalid Title")
            return getValidTitle()
        }
    }

    private fun editDescription(task: Task) {
        val newDescription = getValidDescription()
        manageTaskUseCase.editTaskDescription(task.id, newDescription)
    }

    private fun getValidDescription(): String {
        val userInput = cliReader.getUserInput("New Description: ")
        if (userInput.isNotBlank() && userInput.length <= MAX_DESCRIPTION_LENGTH) return userInput
        else {
            printLn("Invalid description")
            return getValidDescription()
        }
    }

    private fun editState(task: Task) {

        val currentProjectStates = catchDataMemoryRepository.getSelectedProject()?.states ?: run {
            printLn("no project selected")
            return
        }

        if (currentProjectStates.isEmpty()) {
            printLn("no states available")
            return
        }

        printProjectState(currentProjectStates)
        val newState = getValidState(currentProjectStates)

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

    private fun deleteTask(task: Task) {
        if (isCancelDelete()) return

        manageTaskUseCase.deleteTask(task.id)
    }

    private fun isCancelDelete(): Boolean {
        printLn(")")

        return when (cliReader.getUserInput("Are you sure to delete the task? (y/n): ")) {
            "y" -> false
            "n" -> true
            else -> {
                printLn("Invalid input")
                isCancelDelete()
            }
        }
    }

    private fun printLn(message: String) {
        cliPrinter.cliPrintLn(message)
    }

    private companion object {
        val OPTIONS_LIST = listOf("0", "1", "2", "3", "4")
        const val MAX_TITLE_LENGTH = 20
        const val MAX_DESCRIPTION_LENGTH = 150
    }
}