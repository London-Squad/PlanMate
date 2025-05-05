package ui.projectView

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.entities.Project
import logic.entities.State
import logic.entities.Task
import logic.useCases.ProjectUseCases
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import ui.taskManagementView.TaskManagementView
import java.util.UUID

class ProjectTasksViewTest {

    private lateinit var cliPrinter: CLIPrinter
    private lateinit var cliReader: CLIReader
    private lateinit var taskManagementView: TaskManagementView
    private lateinit var projectUseCases: ProjectUseCases
    private lateinit var projectTasksView: ProjectTasksView
    private lateinit var project: Project
    private lateinit var task: Task
    private lateinit var state: State

    @BeforeEach
    fun setUp() {
        cliPrinter = mockk(relaxed = true)
        cliReader = mockk()
        taskManagementView = mockk()
        projectUseCases = mockk(relaxed = true)
        project = mockk()
        task = mockk()
        state = mockk()

        projectTasksView =
            ProjectTasksView(cliPrinter, cliReader, projectUseCases, taskManagementView)

        every { project.title } returns "Test Project"
        every { project.tasks } returns emptyList()
        every { project.tasksStates } returns listOf(state)
        every { project.id } returns UUID.randomUUID()

        every { task.title } returns "Test Task"
        every { task.description } returns "Test Description"
        every { task.state } returns state
        every { state.title } returns "TODO"
        every { state.id } returns UUID.randomUUID()
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
    fun `should return when user enters 0`() {
        // Given
        every { cliReader.getValidUserInput(any(), any(), any()) } returns "0"

        // When
        projectTasksView.manageTasks(project)

        // Then
        verify { cliReader.getValidUserInput(any(), any(), any()) }
    }

    @Test
    fun `should call addNewTask when user enters add with no states`() {
        // Given
        every { project.tasksStates } returns emptyList()
        every { cliReader.getValidUserInput(any(), any(), any()) } returns "add"

        // When
        projectTasksView.manageTasks(project)

        // Then
        verify { cliPrinter.cliPrintLn("No states available for this project. Cannot create task.") }
    }

    @Test
    fun `should call taskManagementView when user enters valid task number`() {
        // Given
        every { project.tasks } returns listOf(task)
        every { cliReader.getValidUserInput(any(), any(), any()) } returns "1"
        every { taskManagementView.start(task.id, project) } returns Unit

        // When
        projectTasksView.manageTasks(project)

        // Then
        verify { taskManagementView.start(task.id, project) }
    }

    @Test
    fun `should display prompt for task selection when tasks exist`() {
        // Given
        every { project.tasks } returns listOf(task)
        every { cliReader.getValidUserInput(any(), any(), any()) } returns "0"

        // When
        projectTasksView.manageTasks(project)

        // Then
        verify { cliPrinter.cliPrintLn("Enter a task number to select (1-1), 'add' to add a new task") }
    }

    @Test
    fun `should display prompt for add only when no tasks exist`() {
        // Given
        every { project.tasks } returns emptyList()
        every { cliReader.getValidUserInput(any(), any(), any()) } returns "0"

        // When
        projectTasksView.manageTasks(project)

        // Then
        verify { cliPrinter.cliPrintLn("Type 'add' to add a new task") }
    }

    @Test
    fun `should create new task with user input and start taskManagementView`() {
        // Given
        every { project.tasks } returns emptyList()
        every { project.tasksStates } returns listOf(state)
        every { cliReader.getValidUserInput(any(), "Enter your choice: ", any()) } returns "add"
        every { cliReader.getValidUserInput(any(), "Enter task title: ", any()) } returns "New Task"
        every { cliReader.getValidUserInput(any(), "Enter task description: ", any()) } returns "New Description"
        every { taskManagementView.start(any(), any()) } returns Unit
        every { project.copy(tasks = any()) } returns project

        // When
        projectTasksView.manageTasks(project)

        // Then
        verify { cliPrinter.cliPrintLn("Task created. You can now edit it.") }
        verify { taskManagementView.start(any(), project) }
        verify { projectUseCases.updateProject(project) }
    }
}