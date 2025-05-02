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
    }

    private fun String.duplicate(numberOfDuplication: Int) =
        List(numberOfDuplication) { this }.joinToString(separator = "")
}