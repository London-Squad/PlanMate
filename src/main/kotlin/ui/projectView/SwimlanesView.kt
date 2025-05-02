package ui.projectView

import logic.entities.Project
import ui.cliPrintersAndReaders.CLIPrinter

class SwimlanesView(
    private val cliPrinter: CLIPrinter
) {

    fun displaySwimlanes(project: Project) {
        cliPrinter.printHeader("Swimlanes: ${project.title}")
        if (project.states.isEmpty()) {
            cliPrinter.cliPrintLn("No states defined for this project.")
            return
        }

        val tasksByState = project.states.associateWith { state ->
            project.tasks.filter { it.state.id == state.id }
        }

        val maxTasks = tasksByState.values.maxOfOrNull { it.size } ?: 0
        val columnWidth = 25 // Adjust width to fit your terminal nicely
        val separator = "|"

        // Print header
        project.states.forEach { state ->
            val stateHeader = state.title.take(columnWidth - 1).padEnd(columnWidth)
            cliPrinter.cliPrint("$stateHeader$separator")
        }
        cliPrinter.cliPrintLn("")

        // Print separator row
        project.states.forEach { _ ->
            cliPrinter.cliPrint("-".repeat(columnWidth) + separator)
        }
        cliPrinter.cliPrintLn("")

        // Print task rows
        for (row in 0 until maxTasks) {
            project.states.forEach { state ->
                val tasks = tasksByState[state] ?: emptyList()
                val taskTitle = if (row < tasks.size) {
                    tasks[row].title.take(columnWidth - 1).padEnd(columnWidth)
                } else {
                    "".padEnd(columnWidth)
                }
                cliPrinter.cliPrint("$taskTitle$separator")
            }
            cliPrinter.cliPrintLn("")
        }
    }
}