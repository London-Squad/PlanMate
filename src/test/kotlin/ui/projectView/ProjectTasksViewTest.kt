package ui.projectView

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.entities.Project
import logic.entities.State
import logic.entities.Task
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import ui.taskManagementView.TaskManagementView

class ProjectTasksViewTest {

    private lateinit var cliPrinter: CLIPrinter
    private lateinit var cliReader: CLIReader
    private lateinit var taskManagementView: TaskManagementView
    private lateinit var projectTasksView: ProjectTasksView
    private lateinit var project: Project
    private lateinit var task: Task
    private lateinit var state: State

    @BeforeEach
    fun setUp() {
        cliPrinter = mockk(relaxed = true)
        cliReader = mockk()
        taskManagementView = mockk()
        project = mockk()
        task = mockk()
        state = mockk()

        projectTasksView = ProjectTasksView(cliPrinter, cliReader, taskManagementView)

        every { project.title } returns "Test Project"
        every { project.tasks } returns emptyList()

        every { task.title } returns "Test Task"
        every { task.description } returns "Test Description"
        every { task.state } returns state
        every { state.title } returns "TODO"
    }

    @Test
    fun `should display no tasks message when project has no tasks`() {
        // Given
        every { project.tasks } returns emptyList()
        every { cliReader.getValidUserInput(any(), any(), any()) } returns "0"

        // When
        projectTasksView.manageTasks(project)

        // Then
        verify { cliPrinter.cliPrintLn("No tasks available in this project.") }
    }

    @Test
    fun `should display tasks when project has tasks`() {
        // Given
        every { project.tasks } returns listOf(task)
        every { cliReader.getValidUserInput(any(), any(), any()) } returns "0"

        // When
        projectTasksView.manageTasks(project)

        // Then
        verify { cliPrinter.cliPrintLn("Current tasks:") }
        verify { cliPrinter.cliPrintLn("1. Test Task (State: TODO)") }
        verify { cliPrinter.cliPrintLn("   Description: Test Description") }
    }

    @Test
    fun `should return when user selects option 0`() {
        // Given
        every { cliReader.getValidUserInput(any(), any(), any()) } returns "0"

        // When
        projectTasksView.manageTasks(project)

        // Then
        verify { cliReader.getValidUserInput(any(), any(), any()) }
    }

    @Test
    fun `should call addNewTask when user selects option 1`() {
        // Given
        every { cliReader.getValidUserInput(any(), any(), any()) } returns "1"

        // When
        projectTasksView.manageTasks(project)

        // Then
        verify { cliReader.getValidUserInput(any(), any(), any()) }
    }

    @Test
    fun `should call selectTask when user selects option 2`() {
        // Given
        every { cliReader.getValidUserInput(any(), any(), any()) } returns "2"

        // When
        projectTasksView.manageTasks(project)

        // Then
        verify { cliReader.getValidUserInput(any(), any(), any()) }
    }

    @Test
    fun `should display no tasks message in selectTask when project has no tasks`() {
        // Given
        every { project.tasks } returns emptyList()
        every { cliReader.getValidUserInput(any(), any(), any()) } returns "2"

        // When
        projectTasksView.manageTasks(project)

        // Then
        verify { cliPrinter.cliPrintLn("No tasks available to select.") }
    }

    @Test
    fun `should call taskManagementView when user selects a task`() {
        // Given
        every { project.tasks } returns listOf(task)
        every { cliReader.getValidUserInput(any(), any(), any()) } returnsMany listOf("2", "1")
        every { taskManagementView.start(task, project) } returns Unit

        // When
        projectTasksView.manageTasks(project)

        // Then
        verify { taskManagementView.start(task, project) }
    }
}