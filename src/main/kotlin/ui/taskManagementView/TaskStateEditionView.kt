package ui.taskManagementView

import logic.entities.TaskState
import logic.useCases.ManageStateUseCase
import logic.useCases.ManageTaskUseCase
import ui.BaseView
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import java.util.*

class TaskStateEditionView(
    private val cliReader: CLIReader,
    private val cliPrinter: CLIPrinter,
    private val manageTaskUseCase: ManageTaskUseCase,
    private val manageStateUseCase: ManageStateUseCase,
) : BaseView(cliPrinter) {

    private lateinit var taskStatesOfProject: List<TaskState>

    fun editState(taskId: UUID, projectId: UUID) {

        makeRequest(
            request = { fetchTaskStates(projectId) },
            onSuccess = {
                printProjectState()
                selectTaskState(taskId)
            }
        )
    }

    private fun selectTaskState(taskId: UUID) {
        val newStateIndex = getUserChoice() - 1

        makeRequest({ manageTaskUseCase.editTaskState(taskId, taskStatesOfProject[newStateIndex].id) })
    }

    private suspend fun fetchTaskStates(projectId: UUID) {
        taskStatesOfProject = manageStateUseCase.getTaskStatesByProjectId(projectId)
    }

    private fun getUserChoice(): Int {
        return cliReader.getValidInputNumberInRange(min = 1, max = taskStatesOfProject.size)
    }

    private fun printProjectState() {
        if (taskStatesOfProject.isEmpty()) {
            cliPrinter.cliPrintLn("no states available")
            return
        }

        taskStatesOfProject.forEachIndexed { index, state ->
            cliPrinter.cliPrintLn("${index + 1}. ${state.title}")
        }
    }
}