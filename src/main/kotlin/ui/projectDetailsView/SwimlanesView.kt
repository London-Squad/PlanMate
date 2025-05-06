package ui.projectDetailsView

import logic.entities.Project
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.cliTable.CLITablePrinter
import ui.cliPrintersAndReaders.cliTable.InvalidTableInput
import logic.entities.TaskState

class SwimlanesView(
    private val cliPrinter: CLIPrinter,
    private val cliTablePrinter: CLITablePrinter = CLITablePrinter(cliPrinter)
) {

    fun displaySwimlanes(project: Project) {
        printHeader(project)

        if (hasNoStates(project)) {
            cliPrinter.cliPrintLn("No states defined for this project.")
            return
        }

        val tasksByState = groupTasksTitlesByState(project)
        val maxTasks = getMaxTaskCount(tasksByState)
        val headers = getHeaders(project.tasksStates)
        val data = buildData(tasksByState, maxTasks, project.tasksStates)
        val columnWidths = List(project.tasksStates.size) { COLUMN_WIDTH }

        displayTable(headers, data, columnWidths)
    }

    private fun printHeader(project: Project) {
        cliPrinter.printHeader("Project: ${project.title}")
    }

    private fun hasNoStates(project: Project) = project.tasksStates.isEmpty()

    private fun groupTasksTitlesByState(project: Project): Map<TaskState, List<String>> {
        return project.tasksStates.associateWith { state ->
            project.tasks
                .mapIndexed { index, task -> Pair("${index + 1}. ${task.title}", task.taskState) }
                .filter { it.second == state }
                .map { it.first }
        }
    }

    private fun getMaxTaskCount(tasksByTaskState: Map<TaskState, List<String>>): Int {
        return tasksByTaskState.values.maxOfOrNull { it.size } ?: 0
    }

    private fun getHeaders(tasksStates: List<TaskState>): List<String> {
        return tasksStates.map { it.title }
    }

    private fun buildData(
        tasksTitlesByTaskState: Map<TaskState, List<String>>,
        maxTasks: Int,
        tasksStates: List<TaskState>
    ): List<List<String>> {
        val data = mutableListOf<List<String>>()
        for (row in 0 until maxTasks) {
            val rowData = tasksStates.map { state ->
                val tasksTitles = tasksTitlesByTaskState[state] ?: emptyList()
                if (row < tasksTitles.size) {
                    val taskTitle = tasksTitles[row]
                    taskTitle
                } else {
                    ""
                }
            }
            data.add(rowData)
        }
        return data
    }

    private fun displayTable(headers: List<String>, data: List<List<String>>, columnWidths: List<Int>) {
        try {
            cliTablePrinter(headers, data, columnWidths)
        } catch (e: InvalidTableInput) {
            cliPrinter.cliPrintLn("Error displaying swimlanes: ${e.message}")
        }
    }

    private companion object {
        const val COLUMN_WIDTH = 30
    }
}