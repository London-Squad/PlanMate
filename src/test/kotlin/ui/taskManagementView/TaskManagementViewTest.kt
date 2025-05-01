package ui.taskManagementView

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.entities.Project
import logic.entities.State
import logic.entities.Task
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import java.util.*

class TaskManagementViewTest {

    private lateinit var taskManagementView: TaskManagementView
    private lateinit var taskTitleEditionView: TaskTitleEditionView
    private lateinit var taskDescriptionEditionView: TaskDescriptionEditionView
    private lateinit var taskStateEditionView: TaskStateEditionView
    private lateinit var taskDeletionView: TaskDeletionView
    private lateinit var cliPrinter: CLIPrinter
    private lateinit var cliReader: CLIReader

    @BeforeEach
    fun setup() {
        taskTitleEditionView = mockk(relaxed = true)
        taskDescriptionEditionView = mockk(relaxed = true)
        taskStateEditionView = mockk(relaxed = true)
        taskDeletionView = mockk(relaxed = true)
        cliPrinter = mockk(relaxed = true)
        cliReader = mockk(relaxed = true)

        taskManagementView = TaskManagementView(
            cliReader,
            cliPrinter,
            taskTitleEditionView,
            taskDescriptionEditionView,
            taskStateEditionView,
            taskDeletionView
        )
    }

    private val statesList = listOf(
        State(UUID.randomUUID(), "Todo", "Todo description"),
        State(UUID.randomUUID(), "Done", "Done description"),
        State(UUID.randomUUID(), "In-progress", "In-progress description"),
    )

    private val tasks = listOf(
        Task(UUID.randomUUID(), "Fake Task 1", description = "description1", statesList[0]),
        Task(UUID.randomUUID(), "Fake Task 2", description = "description2", statesList[1]),
        Task(UUID.randomUUID(), "Fake Task 3", description = "description3", statesList[2]),
    )
    private val project = Project(
        UUID.randomUUID(), "Fake Project", "description",
        tasks = tasks, states = statesList
    )

    @Test
    fun `start should print task when there is selected task`() {
        every { cliReader.getUserInput(any()) } returns "0"

        taskManagementView.start(tasks[0], project)

        verify(exactly = 1) {
            printLn("Task: ${tasks[0].title}")
            printLn("Description: ${tasks[0].description}")
            printLn("State: ${tasks[0].state.title}")
        }
    }

    @Test
    fun `start should print task management options title option when there is selected task`() {
        every { cliReader.getUserInput(any()) } returns "0"

        taskManagementView.start(tasks[0], project)

        verify(exactly = 1) {
            printLn("1. Edit Title")
            printLn("2. Edit description")
            printLn("3. Edit state")
            printLn("4. delete task")
            printLn("0. back")
        }
    }

    @Test
    fun `start should ask for user option`() {
        every { cliReader.getUserInput(any()) } returns "0"

        taskManagementView.start(tasks[0], project)

        verify(exactly = 1) { cliReader.getUserInput("your option:") }
    }

    @Test
    fun `start should go to editTitleView when user input is 1`() {
        every { cliReader.getUserInput(any()) } answers { "1" } andThenAnswer { "0" }

        taskManagementView.start(tasks[0], project)

        verify(exactly = 1) { taskTitleEditionView.editTitle(tasks[0]) }
    }

    @Test
    fun `start should go to editDescriptionView when user input is 2`() {
        every { cliReader.getUserInput(any()) } answers { "2" } andThenAnswer { "0" }

        taskManagementView.start(tasks[0], project)

        verify(exactly = 1) { taskDescriptionEditionView.editDescription(tasks[0]) }
    }

    @Test
    fun `start should go to editTaskStateView when user input is 3`() {
        every { cliReader.getUserInput(any()) } answers { "3" } andThenAnswer { "0" }

        taskManagementView.start(tasks[0], project)

        verify(exactly = 1) { taskStateEditionView.editState(tasks[0], project.states) }
    }

    @Test
    fun `start should go to deleteTaskView when user input is 4`() {
        every { cliReader.getUserInput(any()) } answers { "4" } andThenAnswer { "0" }

        taskManagementView.start(tasks[0], project)

        verify(exactly = 1) { taskDeletionView.deleteTask(tasks[0]) }
    }


    @ParameterizedTest
    @CsvSource(
        "5, 6", "srg, m ", "6576, 55", " y,  n  "
    )
    fun `start should print invalid input when user input is not an option`(firstInput: String, secondInput: String) {
        every { cliReader.getUserInput(any()) } answers { firstInput } andThenAnswer { secondInput } andThenAnswer { "0" }

        taskManagementView.start(tasks[0], project)

        verify(exactly = 2) {
            printLn("Invalid option")
        }
    }

    private fun printLn(message: String) {
        cliPrinter.cliPrintLn(message)
    }
}