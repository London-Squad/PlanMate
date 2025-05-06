package ui.projectDetailsView

import logic.entities.Project
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.cliTable.CLITablePrinter
import logic.entities.TaskState
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
        val headers = getHeaders(project.tasksStates)
        val data = buildData(tasksByState, maxTasks, project.tasksStates)
        val columnWidths = calculateColumnWidths(headers, data)

        displayTable(headers, data, columnWidths)
    }

    private fun printHeader(project: Project) {
        cliPrinter.printHeader("Project: ${project.title}")
    }

    private fun hasNoStates(project: Project) = project.tasksStates.isEmpty()

    private fun groupTasksByState(project: Project): Map<TaskState, List<Task>> {
        return project.tasksStates.associateWith { state ->
            project.tasks.filter { it.taskState.id == state.id }
        }
    }

    private fun getMaxTaskCount(tasksByTaskState: Map<TaskState, List<Task>>): Int {
        return tasksByTaskState.values.maxOfOrNull { it.size } ?: 0
    }

    private fun getHeaders(tasksStates: List<TaskState>): List<String> {
        return tasksStates.map { it.title }
    }

    private fun buildData(
        tasksByTaskState: Map<TaskState, List<Task>>,
        maxTasks: Int,
        tasksStates: List<TaskState>
    ): List<List<String>> {
        val data = mutableListOf<List<String>>()
        var taskNumber = 1
        for (row in 0 until maxTasks) {
            val rowData = tasksStates.map { state ->
                val tasks = tasksByTaskState[state] ?: emptyList()
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
            cliTablePrinter(headers, data, columnWidths)
    }
}