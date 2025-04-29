package ui.projectView

import logic.entities.Project

import ui.View
import ui.cLIPrintersAndReaders.CLIPrinter
import ui.cLIPrintersAndReaders.CLIReader
import java.util.UUID

class ProjectView(
    private val project: Project,
    private val cliPrinter: CLIPrinter,
    private val cliReader: CLIReader
) : View {

    override fun start() {
        displaySwimlanes()
    }

    private fun displaySwimlanes() {
        cliPrinter.printHeader("Project: ${project.title}")
        if (project.states.isEmpty()) {
            cliPrinter.cliPrintLn("No states defined for this project.")
            return
        }

        val tasksByState = project.states.associateWith { state ->
            project.tasks.filter { it.state == state }
        }

        val maxTasks = tasksByState.values.maxOfOrNull { it.size } ?: 0
        //companion
        val columnWidth = 30
        val separator = "|"

        project.states.forEach { state ->
            val stateHeader = state.title.padEnd(columnWidth - 2).take(columnWidth - 2)
            cliPrinter.cliPrint(" $stateHeader $separator")
        }
        cliPrinter.cliPrintLn("")

        project.states.forEach {
            cliPrinter.cliPrint("-".duplicate(columnWidth) + separator)
        }
        cliPrinter.cliPrintLn("")

        for (row in 0 until maxTasks) {
            project.states.forEach { state ->
                val tasks = tasksByState[state] ?: emptyList()
                if (row < tasks.size) {
                    val task = tasks[row]
                    val taskTitle = task.title.take(columnWidth - 4).padEnd(columnWidth - 4)
                    cliPrinter.cliPrint(" $taskTitle $separator")
                } else {
                    cliPrinter.cliPrint("".padEnd(columnWidth) + separator)
                }
            }
            cliPrinter.cliPrintLn("")
        }

        cliPrinter.cliPrintLn("\nSelect a task to view details (enter task ID or 'back' to return): ")
        val input = cliReader.getUserInput("Task ID: ").trim()
        if (input.lowercase() == "back") return

        val taskId = UUID.fromString(input)
        val task = project.tasks.find { it.id == taskId }
        if (task != null) {
            cliPrinter.cliPrintLn("\nTask Details:")
            cliPrinter.cliPrintLn("ID: ${task.id}")
            cliPrinter.cliPrintLn("Title: ${task.title}")
            cliPrinter.cliPrintLn("Description: ${task.description}")
            cliPrinter.cliPrintLn("State: ${task.state.title}")
        } else {
            cliPrinter.cliPrintLn("Task not found.")
        }

    }

    private fun String.duplicate(numberOfDuplication: Int) =
        List(numberOfDuplication) { this }.joinToString(separator = "")
}