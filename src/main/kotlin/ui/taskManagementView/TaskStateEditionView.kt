package ui.taskManagementView

import logic.entities.TaskState
import logic.useCases.ManageStateUseCase
import logic.useCases.ManageTaskUseCase
import ui.BaseView
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import java.util.UUID

class TaskStateEditionView(
    private val cliReader: CLIReader,
    private val cliPrinter: CLIPrinter,
    private val manageTaskUseCase: ManageTaskUseCase,
    private val manageStateUseCase: ManageStateUseCase,
) : BaseView(cliPrinter) {

    private lateinit var tasksStatesOfProject: List<TaskState>

    fun editState(taskId: UUID, projectId: UUID) {

        tryCall({ fetchTaskStates(projectId) }).also { success -> if (!success) return }

        if (tasksStatesOfProject.isEmpty()) {
            cliPrinter.cliPrintLn("no states available")
            return
        }

        printProjectState(tasksStatesOfProject)

        val newStateIndex = getUserChoice() - 1

        tryCall({
            manageTaskUseCase.editTaskState(taskId, tasksStatesOfProject[newStateIndex].id)
        })
    }

    private fun fetchTaskStates(projectId: UUID) {
        tasksStatesOfProject = manageStateUseCase.getTaskStatesByProjectId(projectId)
    }

    private fun getUserChoice(): Int {
        return cliReader.getValidInputNumberInRange(min = 1, max = tasksStatesOfProject.size)
    }

    private fun printProjectState(tasksStates: List<TaskState>) {
        tasksStates.forEachIndexed { index, state ->
            cliPrinter.cliPrintLn("${index + 1}. ${state.title}")
        }
    }
}