package ui.projectsDashboardView

import io.mockk.*
import logic.entities.Project
import logic.entities.User
import logic.useCases.CreateProjectUseCase
import logic.useCases.ManageProjectUseCase
import ui.ViewExceptionHandler
import ui.cliPrintersAndReaders.*
import ui.cliPrintersAndReaders.cliTable.CLITablePrinter
import ui.projectDetailsView.ProjectDetailsView
import kotlin.test.BeforeTest
import kotlin.test.Test

class ProjectsDashboardViewTest {

    private lateinit var cliPrinter: CLIPrinter
    private lateinit var cliReader: CLIReader
    private lateinit var projectInputReader: ProjectInputReader
    private lateinit var manageProjectUseCase: ManageProjectUseCase
    private lateinit var createProjectUseCase: CreateProjectUseCase
    private lateinit var projectView: ProjectDetailsView
    private lateinit var exceptionHandler: ViewExceptionHandler
    private lateinit var cliTablePrinter: CLITablePrinter

    private lateinit var dashboard: ProjectsDashboardView

    private val project = Project(
        title = "Test Title",
        description = "Test Desc",
        tasks = emptyList(),
        tasksStates = emptyList()
    )

    @BeforeTest
    fun setUp() {
        cliPrinter = mockk(relaxed = true)
        cliReader = mockk()
        projectInputReader = mockk()
        manageProjectUseCase = mockk()
        createProjectUseCase = mockk(relaxed = true)
        projectView = mockk(relaxed = true)
        exceptionHandler = mockk()
        cliTablePrinter = mockk(relaxed = true)

        dashboard = ProjectsDashboardView(
            cliPrinter,
            cliReader,
            projectInputReader,
            manageProjectUseCase,
            createProjectUseCase,
            projectView,
            exceptionHandler,
            cliTablePrinter
        )
    }

    @Test
    fun `start should print header`() {
        // given
        every { exceptionHandler.tryCall(any()) } answers {
            (firstArg() as () -> Unit).invoke(); true
        }
        every { manageProjectUseCase.getAllProjects() } returns emptyList()
        every { cliReader.getValidInputNumberInRange(any()) } returns 0

        // when
        dashboard.start(User.Type.MATE)

        // then
        verify { cliPrinter.printHeader("Projects Dashboard Menu") }
    }

    @Test
    fun `getProjects should assign list if call succeeds`() {
        // given
        every { exceptionHandler.tryCall(any()) } answers {
            (firstArg() as () -> Unit).invoke(); true
        }
        every { manageProjectUseCase.getAllProjects() } returns listOf(project)
        every { cliReader.getValidInputNumberInRange(any()) } returns 0

        // when
        dashboard.start(User.Type.MATE)

        // then
        verify { manageProjectUseCase.getAllProjects() }
    }

    @Test
    fun `printProjects should call table printer if projects exist`() {
        // given
        every { exceptionHandler.tryCall(any()) } answers {
            (firstArg() as () -> Unit).invoke(); true
        }
        every { manageProjectUseCase.getAllProjects() } returns listOf(project)
        every { cliReader.getValidInputNumberInRange(any()) } returns 0

        // when
        dashboard.start(User.Type.MATE)

        // then
        verify { cliTablePrinter(any(), any(), any()) }
    }

    @Test
    fun `printProjects should print no projects message if list is empty`() {
        // given
        every { exceptionHandler.tryCall(any()) } answers {
            (firstArg() as () -> Unit).invoke(); true
        }
        every { manageProjectUseCase.getAllProjects() } returns emptyList()
        every { cliReader.getValidInputNumberInRange(any()) } returns 0

        // when
        dashboard.start(User.Type.MATE)

        // then
        verify { cliPrinter.cliPrintLn("No projects found.") }
    }

    @Test
    fun `createProject should call createProjectUseCase`() {
        // given
        every { cliReader.getValidInputNumberInRange(any()) } returnsMany listOf(2, 0)
        every { exceptionHandler.tryCall(any()) } answers {
            (firstArg() as () -> Unit).invoke(); true
        }
        every { manageProjectUseCase.getAllProjects() } returns emptyList()
        every { projectInputReader.getValidProjectTitle() } returns "Title"
        every { projectInputReader.getValidProjectDescription() } returns "Desc"

        // when
        dashboard.start(User.Type.ADMIN)

        // then
        verify { createProjectUseCase.createProject("Title", "Desc") }
    }


    @Test
    fun `selectProject should call projectView if projects exist`() {
        // given
        every { cliReader.getValidInputNumberInRange(any()) } returnsMany listOf(1, 1, 0)
        every { manageProjectUseCase.getAllProjects() } returns listOf(project)
        every { exceptionHandler.tryCall(any()) } answers {
            (firstArg() as () -> Unit).invoke(); true
        }

        // when
        dashboard.start(User.Type.MATE)

        // then
        verify { projectView.start(project.id, User.Type.MATE) }
    }

    @Test
    fun `getValidUserInput should return proper range for mate`() {
        // given
        every { cliReader.getValidInputNumberInRange(1) } returns 0
        every { exceptionHandler.tryCall(any()) } returns true
        every { manageProjectUseCase.getAllProjects() } returns emptyList()

        // when
        dashboard.start(User.Type.MATE)

        // then
        verify { cliReader.getValidInputNumberInRange(1) }
    }
}
