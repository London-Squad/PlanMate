package ui.projectsDashboardView

import io.mockk.*
import logic.entities.User
import logic.useCases.CreateProjectUseCase
import logic.useCases.ManageProjectUseCase
import ui.ViewExceptionHandler
import ui.cliPrintersAndReaders.*
import ui.cliPrintersAndReaders.cliTable.CLITablePrinter
import ui.projectDetailsView.ProjectDetailsView
import ui.taskManagementView.FakeProjectData
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

        every { exceptionHandler.tryCall(any()) } answers {
            (firstArg() as () -> Unit).invoke(); true
        }
    }

    @Test
    fun `start should print header`() {
        // given
        every { manageProjectUseCase.getAllProjects() } returns emptyList()
        every { cliReader.getValidInputNumberInRange(any()) } returns 0

        // when
        dashboard.start(User.Type.MATE)

        // then
        verify (exactly = 1) { cliPrinter.printHeader("Projects Dashboard Menu") }
    }

    @Test
    fun `getProjects should assign list if call succeeds`() {
        // given
        every { manageProjectUseCase.getAllProjects() } returns listOf(FakeProjectData.project)
        every { cliReader.getValidInputNumberInRange(any()) } returns 0

        // when
        dashboard.start(User.Type.MATE)

        // then
        verify (exactly = 1) { manageProjectUseCase.getAllProjects() }
    }

    @Test
    fun `printProjects should call table printer if projects exist`() {
        // given
        every { manageProjectUseCase.getAllProjects() } returns listOf(FakeProjectData.project)
        every { cliReader.getValidInputNumberInRange(any()) } returns 0

        // when
        dashboard.start(User.Type.MATE)

        // then
        verify (exactly = 1) { cliTablePrinter(any(), any(), any()) }
    }

    @Test
    fun `printProjects should print no projects message if list is empty`() {
        // given
        every { manageProjectUseCase.getAllProjects() } returns emptyList()
        every { cliReader.getValidInputNumberInRange(any()) } returns 0

        // when
        dashboard.start(User.Type.MATE)

        // then
        verify (exactly = 1) { cliPrinter.cliPrintLn("No projects found.") }
    }

    @Test
    fun `createProject should call createProjectUseCase`() {
        // given
        val title = "Title"
        val description = "Desc"
        every { cliReader.getValidInputNumberInRange(any()) } returnsMany listOf(2, 0)
        every { manageProjectUseCase.getAllProjects() } returns emptyList()
        every { projectInputReader.getValidProjectTitle() } returns title
        every { projectInputReader.getValidProjectDescription() } returns description

        // when
        dashboard.start(User.Type.ADMIN)

        // then
        verify (exactly = 1) { createProjectUseCase.createProject(title, description) }
    }


    @Test
    fun `selectProject should call projectView if projects exist`() {
        // given
        every { cliReader.getValidInputNumberInRange(any()) } returnsMany listOf(1, 1, 0)
        every { manageProjectUseCase.getAllProjects() } returns listOf(FakeProjectData.project)

        // when
        dashboard.start(User.Type.MATE)

        // then
        verify (exactly = 1) { projectView.start(FakeProjectData.project.id, User.Type.MATE) }
    }

    @Test
    fun `getValidUserInput should return proper range for mate`() {
        // given
        every { cliReader.getValidInputNumberInRange(1) } returns 0
        every { manageProjectUseCase.getAllProjects() } returns emptyList()

        // when
        dashboard.start(User.Type.MATE)

        // then
        verify (exactly = 1) { cliReader.getValidInputNumberInRange(1) }
    }
}
