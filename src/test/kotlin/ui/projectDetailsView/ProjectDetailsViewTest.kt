//package ui.projectDetailsView
//
//import io.mockk.every
//import io.mockk.mockk
//import io.mockk.verify
//import logic.entities.Project
//import logic.entities.User
//import logic.exceptions.NoLoggedInUserFoundException
//import logic.useCases.GetLoggedInUserUseCase
//import logic.useCases.ProjectUseCases
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.Test
//import ui.cliPrintersAndReaders.CLIPrinter
//import ui.cliPrintersAndReaders.CLIReader
//import ui.logsView.LogsView
//
//class ProjectDetailsViewTest {
//
//    private lateinit var cliPrinter: CLIPrinter
//    private lateinit var cliReader: CLIReader
//
//    private lateinit var swimlanesView: SwimlanesView
//    private lateinit var editProjectView: EditProjectView
//    private lateinit var deleteProjectView: DeleteProjectView
//    private lateinit var projectTasksView: ProjectTasksView
//    private lateinit var projectView: ProjectDetailsView
//    private lateinit var project: Project
//    private lateinit var user: User
//    private lateinit var logsView: LogsView
//    private lateinit var projectUseCases: ProjectUseCases
//    private lateinit var getLoggedInUserUseCase: GetLoggedInUserUseCase
//
//    @BeforeEach
//    fun setUp() {
//        cliPrinter = mockk(relaxed = true)
//        cliReader = mockk()
//        getLoggedInUserUseCase = mockk()
//        swimlanesView = mockk()
//        editProjectView = mockk()
//        deleteProjectView = mockk()
//        projectTasksView = mockk()
//        project = mockk()
//        user = mockk()
//        logsView = mockk()
//        projectUseCases = mockk()
//
//        projectView = ProjectDetailsView(
//            cliPrinter,
//            cliReader,
//            getLoggedInUserUseCase,
//            swimlanesView,
//            editProjectView,
//            deleteProjectView,
//            projectTasksView,
//            projectUseCases,
//            logsView
//        )
//
//        every { project.title } returns "Test Project"
//
//        every { getLoggedInUserUseCase.getLoggedInUser() } returns user
//        every { user.type } returns User.Type.ADMIN
//
//        every { swimlanesView.displaySwimlanes(project) } returns Unit
//    }
//
//    @Test
//    fun `should display error message when user is not logged in`() {
//        // Given
//        every { getLoggedInUserUseCase.getLoggedInUser() } throws NoLoggedInUserFoundException()
//
//        // When
//        projectView.start(project)
//
//        // Then
//        verify { cliPrinter.cliPrintLn(ProjectDetailsView.ERROR_MESSAGE) }
//    }
//
//    @Test
//    fun `should display swimlanes when user is logged in`() {
//        // Given
//        every { cliReader.getValidUserInput(any(), any(), any()) } returns "0"
//
//        // When
//        projectView.start(project)
//
//        // Then
//        verify { swimlanesView.displaySwimlanes(project) }
//    }
//
//    @Test
//    fun `should print basic menu options for non-admin user`() {
//        // Given
//        every { user.type } returns User.Type.MATE
//        every { cliReader.getValidUserInput(any(), any(), any()) } returns "0"
//
//        // When
//        projectView.start(project)
//
//        // Then
//        verify { cliPrinter.cliPrintLn("1. Manage tasks") }
//        verify { cliPrinter.cliPrintLn("2. View project logs") }
//        verify { cliPrinter.cliPrintLn("0. Back to projects") }
//    }
//
//    @Test
//    fun `should print admin options for admin user`() {
//        // Given
//        every { user.type } returns User.Type.ADMIN
//        every { cliReader.getValidUserInput(any(), any(), any()) } returns "0"
//
//        // When
//        projectView.start(project)
//
//        // Then
//        verify { cliPrinter.cliPrintLn("3. Edit project") }
//        verify { cliPrinter.cliPrintLn("4. Delete project") }
//    }
//
//    @Test
//    fun `should return when non-admin user selects option 0`() {
//        // Given
//        every { user.type } returns User.Type.MATE
//        every { cliReader.getValidUserInput(any(), any(), any()) } returns "0"
//
//        // When
//        projectView.start(project)
//
//        // Then
//        verify { cliReader.getValidUserInput(any(), any(), any()) }
//    }
//
//    @Test
//    fun `should call projectTasksView when non-admin user selects option 1`() {
//        // Given
//        every { user.type } returns User.Type.MATE
//        every { cliReader.getValidUserInput(any(), any(), any()) } returns "1"
//        every { projectTasksView.manageTasks(project) } returns project
//
//        // When
//        projectView.start(project)
//
//        // Then
//        verify { projectTasksView.manageTasks(project) }
//    }
//
//    @Test
//    fun `should return when admin user selects option 0`() {
//        // Given
//        every { user.type } returns User.Type.ADMIN
//        every { cliReader.getValidUserInput(any(), any(), any()) } returns "0"
//
//        // When
//        projectView.start(project)
//
//        // Then
//        verify { cliReader.getValidUserInput(any(), any(), any()) }
//    }
//
//    @Test
//    fun `should call projectTasksView when admin user selects option 1`() {
//        // Given
//        every { user.type } returns User.Type.ADMIN
//        every { cliReader.getValidUserInput(any(), any(), any()) } returns "1"
//        every { projectTasksView.manageTasks(project) } returns project
//
//        // When
//        projectView.start(project)
//
//        // Then
//        verify { projectTasksView.manageTasks(project) }
//    }
//
//    @Test
//    fun `should call deleteProjectView when admin user selects option 4`() {
//        // Given
//        every { user.type } returns User.Type.ADMIN
//        every { cliReader.getValidUserInput(any(), any(), any()) } returns "4"
//        every { deleteProjectView.deleteProject(project) } returns Unit
//
//        // When
//        projectView.start(project)
//
//        // Then
//        verify { deleteProjectView.deleteProject(project) }
//    }
//}