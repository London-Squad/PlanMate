package ui.taskManagementView

import logic.entities.Project
import logic.entities.Task
import logic.repositories.TaskRepository
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader

class TaskManagementView(
    private val cliPrinter: CLIPrinter,
    private val taskTitleEditionView: TaskTitleEditionView,
    private val taskDescriptionEditionView: TaskDescriptionEditionView,
    private val taskStateEditionView: TaskStateEditionView,
    private val taskDeletionView: TaskDeletionView,
    private val taskRepository: TaskRepository,
    private val cliReader: CLIReader
) {

    fun start(task: Task, project: Project) {
        printTask(task)
        printOptions()
        selectNextUI(task, project)
    }

    private fun printTask(task: Task) {
        printLn("Task: ${task.title}")
        printLn("Description: ${task.description}")
        printLn("State: ${task.state.title}")
    }

    private fun printOptions() {
        printLn("1. Edit Title")
        printLn("2. Edit description")
        printLn("3. Edit state")
        printLn("4. delete task")
        printLn("0. back")
    }

    private fun selectNextUI(task: Task, project: Project) {
        when (cliReader.getValidUserNumberInRange(MAX_OPTION_NUMBER)) {
            "1" -> {
                taskTitleEditionView.editTitle(task)
                val updatedTask = taskRepository.getTaskByID(task.id) ?: task
                start(updatedTask, project)
            }

            "2" -> {
                taskDescriptionEditionView.editDescription(task)
                val updatedTask = taskRepository.getTaskByID(task.id) ?: task
                start(updatedTask, project)
            }

            "3" -> {
                taskStateEditionView.editState(task, project.states)
                val updatedTask = taskRepository.getTaskByID(task.id) ?: task
                start(updatedTask, project)
            }

            "4" -> {
                taskDeletionView.deleteTask(task)
                return
            }

            "0" -> return
        }
    }


    private fun printLn(message: String) {
        cliPrinter.cliPrintLn(message)
    }

    private companion object {
        const val MAX_OPTION_NUMBER = 4
    }
}