package ui.projectDetailsView

import logic.entities.Project
import logic.entities.Task
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.cliTable.CLITablePrinter
import ui.cliPrintersAndReaders.cliTable.InvalidTableInput
import logic.entities.TaskState
import logic.useCases.GetProjectDetailsUseCase
import java.util.UUID

class SwimlanesView(
    private val cliPrinter: CLIPrinter,
    private val cliTablePrinter: CLITablePrinter,
    private val getProjectDetailsUseCase: GetProjectDetailsUseCase
) {

    suspend fun displaySwimlanes(project: Project) {
        printHeader(project)
        val projectDetails = getProjectDetailsUseCase(project.id)
        val taskStates = projectDetails.taskStates
        val tasksByState = groupTasksByState(projectDetails.tasks, taskStates)
        val maxTasks = tasksByState.values.maxOfOrNull { it.size } ?: 0
        val headers = taskStates.map { it.title }
        val data = buildData(tasksByState, maxTasks, taskStates)
        val columnWidths = List(taskStates.size) { COLUMN_WIDTH }
        displayTable(headers, data, columnWidths)
    }

    private fun printHeader(project: Project) {
        cliPrinter.printHeader("Project: ${project.title}")
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