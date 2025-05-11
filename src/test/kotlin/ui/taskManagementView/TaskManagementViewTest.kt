package ui.taskManagementView

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.useCases.ManageTaskUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ui.BaseView
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import ui.logsView.LogsView

class TaskManagementViewTest {

    private lateinit var taskManagementView: TaskManagementView

    private lateinit var taskTitleEditionView: TaskTitleEditionView
    private lateinit var taskDescriptionEditionView: TaskDescriptionEditionView
    private lateinit var taskStateEditionView: TaskStateEditionView
    private lateinit var baseView: BaseView
    private lateinit var logsView: LogsView
    private lateinit var manageTaskUseCase: ManageTaskUseCase
    private lateinit var taskDeletionView: TaskDeletionView
    private lateinit var cliPrinter: CLIPrinter
    private lateinit var cliReader: CLIReader

    @BeforeEach
    fun setup() {
        taskTitleEditionView = mockk(relaxed = true)
        taskDescriptionEditionView = mockk(relaxed = true)
        taskStateEditionView = mockk(relaxed = true)
        baseView = mockk(relaxed = true)
        logsView = mockk(relaxed = true)
        manageTaskUseCase = mockk(relaxed = true)
        taskDeletionView = mockk(relaxed = true)
        cliPrinter = mockk(relaxed = true)
        cliReader = mockk(relaxed = true)

        taskManagementView = TaskManagementView(
            cliReader,
            cliPrinter,
            taskTitleEditionView,
            taskDescriptionEditionView,
            taskStateEditionView,
            taskDeletionView,
            manageTaskUseCase,
            logsView,
            baseView
        )

        every { manageTaskUseCase.getTaskByID(any()) } returns FakeProjectData.tasks[0]
        every { baseView.tryCall(any()) } answers {
            firstArg<() -> Unit>().invoke()
            true
        }
    }

    @Test
    fun `start should print task when there is selected task`() {
        every { cliReader.getValidInputNumberInRange(any()) } returns 0

        taskManagementView.start(FakeProjectData.tasks[0].id, FakeProjectData.project)

        verify(exactly = 1) {
            cliPrinter.printHeader("Task: ${FakeProjectData.tasks[0].title}")
            printLn("Details:")
            printLn("  - Description: ${FakeProjectData.tasks[0].description}")
            printLn("  - State: ${FakeProjectData.tasks[0].taskState.title}")
            printLn("")
        }
    }

    @Test
    fun `start should print task management options when there is selected task`() {
        every { cliReader.getValidInputNumberInRange(any()) } returns 0

        taskManagementView.start(FakeProjectData.tasks[0].id, FakeProjectData.project)

        verify(exactly = 1) {
            printLn("Options:")
            printLn("1. Edit Title")
            printLn("2. Edit description")
            printLn("3. Edit state")
            printLn("4. Delete task")
            printLn("5. View task logs")
            printLn("0. Back")
        }
    }

    @Test
    fun `start should go to editTitleView when user input is 1`() {
        every { cliReader.getValidInputNumberInRange(any()) } answers { 1 } andThenAnswer { 0 }

        taskManagementView.start(FakeProjectData.tasks[0].id, FakeProjectData.project)

        verify(exactly = 1) { taskTitleEditionView.editTitle(FakeProjectData.tasks[0]) }
    }

    @Test
    fun `start should go to editDescriptionView when user input is 2`() {
        every { cliReader.getValidInputNumberInRange(any()) } answers { 2 } andThenAnswer { 0 }

        taskManagementView.start(FakeProjectData.tasks[0].id, FakeProjectData.project)

        verify(exactly = 1) { taskDescriptionEditionView.editDescription(FakeProjectData.tasks[0]) }
    }

    @Test
    fun `start should go to editTaskStateView when user input is 3`() {
        every { cliReader.getValidInputNumberInRange(any()) } answers { 3 } andThenAnswer { 0 }

        taskManagementView.start(FakeProjectData.tasks[0].id, FakeProjectData.project)

        verify(exactly = 1) { taskStateEditionView.editState(FakeProjectData.tasks[0], FakeProjectData.project.tasksStates) }
    }

    @Test
    fun `start should go to deleteTaskView when user input is 4`() {
        every { cliReader.getValidInputNumberInRange(any(), any()) } answers { 4 } andThenAnswer { 0 }

        taskManagementView.start(FakeProjectData.tasks[0].id, FakeProjectData.project)

        verify(exactly = 1) { taskDeletionView.deleteTask(FakeProjectData.tasks[0]) }
    }

    @Test
    fun `start should go to task logsView when user input is 5`() {
        every { cliReader.getValidInputNumberInRange(any(), any()) } answers { 5 } andThenAnswer { 0 }

        taskManagementView.start(FakeProjectData.tasks[0].id, FakeProjectData.project)

        verify(exactly = 1) { logsView.printLogsByEntityId(FakeProjectData.tasks[0].id) }
    }

    private fun printLn(message: String) {
        cliPrinter.cliPrintLn(message)
    }
}