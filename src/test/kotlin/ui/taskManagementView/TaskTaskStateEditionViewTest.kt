package ui.taskManagementView

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.entities.TaskState
import logic.entities.Task
import logic.useCases.ManageTaskUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ui.ViewExceptionHandler
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import java.util.*

class TaskTaskStateEditionViewTest {

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

    private val task = Task(UUID.randomUUID(), "Fake Task 1", description = "description1")
    private val taskStatesLists = listOf(
        TaskState(UUID.randomUUID(), "Todo", "Todo description"),
        TaskState(UUID.randomUUID(), "Done", "Done description"),
        TaskState(UUID.randomUUID(), "In-progress", "In-progress description"),
    )

    @Test
    fun `editState should return when no state are available`() {

        taskStateEditionView.editState(task, emptyList())

        verify(exactly = 1) {
            cliPrinter.cliPrintLn("no states available")
        }
    }

    @Test
    fun `editState should print states when available`() {
        every { cliReader.getValidInputNumberInRange(any(), any()) } returns 1

        taskStateEditionView.editState(task, taskStatesLists)

        verify(exactly = 1) {
            taskStatesLists.forEachIndexed { index, state ->
                cliPrinter.cliPrintLn("${index + 1}. ${state.title}")
            }
        }
    }
}