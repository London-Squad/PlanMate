package ui.projectDetailsView

import logic.entities.Project
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.cliTable.CLITablePrinter
import ui.cliPrintersAndReaders.cliTable.InvalidTableInput
import logic.entities.State
import logic.entities.Task

class SwimlanesView(
    private val cliPrinter: CLIPrinter,
    private val cliTablePrinter: CLITablePrinter = CLITablePrinter(cliPrinter)
) {

    fun displaySwimlanes(project: Project) {
        cliPrinter.printHeader("Project: ${project.title}")
        printHeader(project)
        if (hasNoStates(project)) {
            cliPrinter.cliPrintLn("No states defined for this project.")
            return
        }

        val tasksByState = groupTasksByState(project)
        val maxTasks = getMaxTaskCount(tasksByState)
        val headers = getHeaders(project.states)
        val data = buildData(tasksByState, maxTasks, project.states)
        val columnWidths = calculateColumnWidths(headers, data)

        displayTable(headers, data, columnWidths)
    }

    private fun printHeader(project: Project) {
        cliPrinter.printHeader("Project: ${project.title}")
    }

    private fun hasNoStates(project: Project) = project.states.isEmpty()

    private fun groupTasksByState(project: Project): Map<State, List<Task>> {
        return project.states.associateWith { state ->
            project.tasks.filter { it.state.id == state.id }
        }
    }

    private fun getMaxTaskCount(tasksByState: Map<State, List<Task>>): Int {
        return tasksByState.values.maxOfOrNull { it.size } ?: 0
    }

    private fun getHeaders(states: List<State>): List<String> {
        return states.map { it.title }
    }

    private fun buildData(
        tasksByState: Map<State, List<Task>>,
        maxTasks: Int,
        states: List<State>
    ): List<List<String>> {
        val data = mutableListOf<List<String>>()
        var taskNumber = 1
        for (row in 0 until maxTasks) {
            val rowData = states.map { state ->
                val tasks = tasksByState[state] ?: emptyList()
                if (row < tasks.size) {
                    val task = tasks[row]
                    val numberedTask = "$taskNumber. ${task.title}"
                    taskNumber++
                    numberedTask
                } else {
                    ""
                }
            }
            data.add(rowData)
        }
        return data
    }

    private fun calculateColumnWidths(headers: List<String>, data: List<List<String>>): List<Int> {
        return headers.mapIndexed { colIndex, header ->
            maxOf(header.length, data.maxOfOrNull { it[colIndex].length } ?: 0)
        }
    }

    private fun displayTable(headers: List<String>, data: List<List<String>>, columnWidths: List<Int>) {
        try {
            cliTablePrinter(headers, data, columnWidths)
        } catch (e: InvalidTableInput) {
            cliPrinter.cliPrintLn("Error displaying swimlanes: ${e.message}")
        }
    }
}