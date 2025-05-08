package ui.taskManagementView

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.entities.Project
import logic.entities.TaskState
import logic.entities.Task
import logic.repositories.TaskRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ui.ViewExceptionHandler
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import ui.logsView.LogsView
import java.util.*

class TaskManagementViewTest {

    private lateinit var taskManagementView: TaskManagementView
    private lateinit var taskTitleEditionView: TaskTitleEditionView
    private lateinit var taskDescriptionEditionView: TaskDescriptionEditionView
    private lateinit var taskStateEditionView: TaskStateEditionView
    private lateinit var viewExceptionHandler: ViewExceptionHandler
    private lateinit var logsView: LogsView
    private lateinit var taskRepository: TaskRepository
    private lateinit var taskDeletionView: TaskDeletionView
    private lateinit var cliPrinter: CLIPrinter
    private lateinit var cliReader: CLIReader

    @BeforeEach
    fun setup() {
        taskTitleEditionView = mockk(relaxed = true)
        taskDescriptionEditionView = mockk(relaxed = true)
        taskStateEditionView = mockk(relaxed = true)
        viewExceptionHandler = mockk(relaxed = true)
        logsView = mockk(relaxed = true)
        taskRepository = mockk(relaxed = true)
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
            taskRepository,
            logsView,
            viewExceptionHandler
        )

        every { taskRepository.getTaskByID(any()) } returns tasks[0]
        every { viewExceptionHandler.tryCall(any()) } answers {
            firstArg<() -> Unit>().invoke()
            true
        }
    }

    private val statesList = listOf(
        TaskState(UUID.randomUUID(), "Todo", "Todo description"),
        TaskState(UUID.randomUUID(), "Done", "Done description"),
        TaskState(UUID.randomUUID(), "In-progress", "In-progress description"),
    )

    private val tasks = listOf(
        Task(UUID.randomUUID(), "Fake Task 1", description = "description1", statesList[0]),
        Task(UUID.randomUUID(), "Fake Task 2", description = "description2", statesList[1]),
        Task(UUID.randomUUID(), "Fake Task 3", description = "description3", statesList[2]),
    )
    private val project = Project(
        UUID.randomUUID(), "Fake Project", "description",
        tasks = tasks, tasksStates = statesList
    )

    @Test
    fun `start should print task when there is selected task`() {
        every { cliReader.getValidInputNumberInRange(any()) } returns 0

        taskManagementView.start(tasks[0].id, project)

        verify(exactly = 1) {
            cliPrinter.printHeader("Task: ${tasks[0].title}")
            printLn("Details:")
            printLn("  - Description: ${tasks[0].description}")
            printLn("  - State: ${tasks[0].taskState.title}")
            printLn("")
        }
    }

    @Test
    fun `start should print task management options title option when there is selected task`() {
        every { cliReader.getValidInputNumberInRange(any()) } returns 0

        taskManagementView.start(tasks[0].id, project)

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

        taskManagementView.start(tasks[0].id, project)

        verify(exactly = 1) { taskTitleEditionView.editTitle(tasks[0]) }
    }

    @Test
    fun `start should go to editDescriptionView when user input is 2`() {
        every { cliReader.getValidInputNumberInRange(any()) } answers { 2 } andThenAnswer { 0 }

        taskManagementView.start(tasks[0].id, project)

        verify(exactly = 1) { taskDescriptionEditionView.editDescription(tasks[0]) }
    }

    @Test
    fun `start should go to editTaskStateView when user input is 3`() {
        every { cliReader.getValidInputNumberInRange(any()) } answers { 3 } andThenAnswer { 0 }

        taskManagementView.start(tasks[0].id, project)

        verify(exactly = 1) { taskStateEditionView.editState(tasks[0], project.tasksStates) }
    }

    @Test
    fun `start should go to deleteTaskView when user input is 4`() {
        every { cliReader.getValidInputNumberInRange(any(), any()) } answers { 4 } andThenAnswer { 0 }

        taskManagementView.start(tasks[0].id, project)

        verify(exactly = 1) { taskDeletionView.deleteTask(tasks[0]) }
    }

    @Test
    fun `start should go to task logsView when user input is 5`() {
        every { cliReader.getValidInputNumberInRange(any(), any()) } answers { 5 } andThenAnswer { 0 }

        taskManagementView.start(tasks[0].id, project)

        verify(exactly = 1) { logsView.printLogsByEntityId(tasks[0].id) }
    }

    private fun printLn(message: String) {
        cliPrinter.cliPrintLn(message)
    }
}