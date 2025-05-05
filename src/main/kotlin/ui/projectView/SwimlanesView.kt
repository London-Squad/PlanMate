package ui.projectView

import logic.entities.Project
import ui.cliPrintersAndReaders.CLIPrinter

class SwimlanesView(
    private val cliPrinter: CLIPrinter
) {

    fun displaySwimlanes(project: Project) {
        cliPrinter.printHeader("Project: ${project.title}")
        if (project.tasksStates.isEmpty()) {
            cliPrinter.cliPrintLn("No states defined for this project.")
            return
        }

        val tasksByState = project.tasksStates.associateWith { state ->
            project.tasks.filter { it.state.id == state.id }
        }

        val maxTasks = tasksByState.values.maxOfOrNull { it.size } ?: 0
        val columnWidth = 25
        val separator = "|"

        project.tasksStates.forEach { state ->
            val stateHeader = state.title.take(columnWidth - 1).padEnd(columnWidth)
            cliPrinter.cliPrint("$stateHeader$separator")
        }
        cliPrinter.cliPrintLn("")

        project.tasksStates.forEach { _ ->
            cliPrinter.cliPrint("-".repeat(columnWidth) + separator)
        }
        cliPrinter.cliPrintLn("")

        for (row in 0 until maxTasks) {
            project.tasksStates.forEach { state ->
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