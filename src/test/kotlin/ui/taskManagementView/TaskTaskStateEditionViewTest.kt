package ui.taskManagementView

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.useCases.ManageTaskUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ui.ViewExceptionHandler
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader

class TaskStateEditionViewTest {

    private lateinit var taskStateEditionView: TaskStateEditionView
    private lateinit var viewExceptionHandler: ViewExceptionHandler
    private lateinit var cliPrinter: CLIPrinter
    private lateinit var cliReader: CLIReader
    private lateinit var manageTaskUseCase: ManageTaskUseCase

    @BeforeEach
    fun setup() {

        manageTaskUseCase = mockk(relaxed = true)
        viewExceptionHandler = mockk(relaxed = true)
        cliPrinter = mockk(relaxed = true)
        cliReader = mockk(relaxed = true)

        taskStateEditionView = TaskStateEditionView(cliReader, cliPrinter, manageTaskUseCase, viewExceptionHandler)

        every { viewExceptionHandler.tryCall(any()) } answers {
            firstArg<() -> Unit>().invoke()
            true
        }
    }

    @Test
    fun `editState should return when no state are available`() {

        taskStateEditionView.editState(FakeProjectData.tasks[0], emptyList())

        verify(exactly = 1) {
            cliPrinter.cliPrintLn("no states available")
        }
    }

    @Test
    fun `editState should call manageTaskUseCase editTaskState and pass the right parameters`() {
        every { cliReader.getValidInputNumberInRange(any(), any()) } returns 1

        taskStateEditionView.editState(FakeProjectData.tasks[0], FakeProjectData.taskStatesLists)

        verify(exactly = 1) {
            manageTaskUseCase.editTaskState(FakeProjectData.tasks[0].id, FakeProjectData.taskStatesLists.first())
        }
    }
}