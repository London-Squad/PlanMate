package ui.projectView

import logic.entities.Project
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.cliTable.CLITablePrinter
import ui.cliPrintersAndReaders.cliTable.InvalidTableInput

class SwimlanesView(
    private val cliPrinter: CLIPrinter, private val cliTablePrinter: CLITablePrinter = CLITablePrinter(cliPrinter)
) {

    fun displaySwimlanes(project: Project) {
        cliPrinter.printHeader("Project: ${project.title}")
        if (project.states.isEmpty()) {
            cliPrinter.cliPrintLn("No states defined for this project.")
            return
        }

        val tasksByState = project.states.associateWith { state ->
            project.tasks.filter { it.state.id == state.id }
        }

        val maxTasks = tasksByState.values.maxOfOrNull { it.size } ?: 0
        val headers = project.states.map { it.title }

        val data = mutableListOf<List<String>>()
        for (row in 0 until maxTasks) {
            val rowData = project.states.map { state ->
                val tasks = tasksByState[state] ?: emptyList()
                if (row < tasks.size) {
                    val task = tasks[row]
                    "${row + 1}. ${task.title}"
                } else {
                    ""
                }
            }
            data.add(rowData)
        }

        val columnWidths = headers.mapIndexed { colIndex, header ->
            maxOf(header.length, data.maxOfOrNull { it[colIndex].length } ?: 0)
        }
        try {
            cliTablePrinter(headers, data, columnWidths)
        } catch (e: InvalidTableInput) {
            cliPrinter.cliPrintLn("Error displaying swimlanes: ${e.message}")
        }
    }
}