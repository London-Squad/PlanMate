package ui.projectDetailsView

import logic.entities.Task
import logic.entities.TaskState
import ui.cliPrintersAndReaders.CLITablePrinter
import java.util.*

class SwimlanesView(
    private val cliTablePrinter: CLITablePrinter,
) {

    fun displaySwimlanes(tasks: List<Task>, taskStates: List<TaskState>) {

        val tasksByState = groupTasksByState(tasks, taskStates)
        val maxTasks = tasksByState.values.maxOfOrNull { it.size } ?: 0
        val headers = taskStates.map { it.title }
        val data = buildData(tasksByState, maxTasks, taskStates)
        val columnWidths = List(taskStates.size) { COLUMN_WIDTH }
        displayTable(headers, data, columnWidths)
    }

    private fun groupTasksByState(tasks: List<Task>, taskStates: List<TaskState>): Map<UUID, List<String>> {
        return taskStates.associate { state ->
            state.id to tasks
                .mapIndexed { index, task -> Pair(task.taskStateId, "${index + 1}. ${task.title}") }
                .filter { it.first == state.id }
                .map { it.second }
        }
    }

    private fun buildData(
        tasksByState: Map<UUID, List<String>>,
        maxTasks: Int,
        tasksStates: List<TaskState>
    ): List<List<String>> {
        return (0 until maxTasks).map { row ->
            tasksStates.map { state ->
                tasksByState[state.id]?.getOrNull(row) ?: ""
            }
        }
    }

    private fun displayTable(headers: List<String>, data: List<List<String>>, columnWidths: List<Int>) {
        cliTablePrinter(headers, data, columnWidths)
    }

    private companion object {
        const val COLUMN_WIDTH = 30
    }
}