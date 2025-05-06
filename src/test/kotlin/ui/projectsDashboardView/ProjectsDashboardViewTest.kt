//package ui.projectsDashboardView
//
//import io.mockk.every
//import io.mockk.mockk
//import io.mockk.verify
//import logic.entities.User
//import logic.exceptions.NoLoggedInUserIsSavedInCacheException
//import logic.useCases.GetLoggedInUserUseCase
//import logic.useCases.ProjectUseCases
//import ui.cliPrintersAndReaders.CLIPrinter
//import ui.cliPrintersAndReaders.CLIReader
//import ui.projectDetailsView.ProjectDetailsView
//import kotlin.test.Test
//
//class ProjectsDashboardViewTest {
//
//    private val cliPrinter: CLIPrinter = mockk(relaxed = true)
//    private val cliReader: CLIReader = mockk()
//    private val projectUseCases: ProjectUseCases = mockk(relaxed = true)
//    private val getLoggedInUserUseCase: GetLoggedInUserUseCase = mockk(relaxed = true)
//    private val projectView: ProjectDetailsView = mockk(relaxed = true)
//    private val projectsView = ProjectsDashboardView(
//        cliPrinter,
//        cliReader,
//        projectUseCases,
//        getLoggedInUserUseCase,
//        projectView
//    )
//
//    @Test
//    fun shouldPrintErrorAndExitWhenNoUserLoggedIn() {
//        // given
//        every { getLoggedInUserUseCase.getLoggedInUser() } throws NoLoggedInUserIsSavedInCacheException()
//
//        // when
//        projectsView.start()
//
//        // then
//        verify { cliPrinter.cliPrintLn("Error: No user logged in. Please log in first.") }
//    }
//
//    @Test
//    fun shouldCallHandleProjectsViewWhenUserLoggedIn() {
//        // given
//        val user = mockk<User>()
//        every { getLoggedInUserUseCase.getLoggedInUser() } returns user
//        every { user.type } returns User.Type.MATE
//        every { projectUseCases.getAllProjects() } returns emptyList()
//        every { cliReader.getUserInput("Choice: ") } returns "back"
//        every { cliReader.getValidUserInput(any(), any(), any()) } returns "0"
//
//        // when
//        projectsView.start()
//
//        // then
//        verify { cliPrinter.printHeader("Projects Menu") }
//    }
//
//    @Test
//    fun shouldLoopUntilMenuInputReturnsFalse() {
//        // given
//        val user = mockk<User>()
//        every { user.type } returns User.Type.MATE
//        every { projectUseCases.getAllProjects() } returns emptyList()
//        every { cliReader.getUserInput("Choice: ") } returns "back"
//        every { cliReader.getValidUserInput(any(), any(), any()) } returnsMany listOf("1", "0")
//
//        // then
//        verify { cliReader.getValidUserInput(any(), eq("Choose an option: "), any()) }
//    }
//
////    @Test
////    fun shouldCreateProjectWithValidInput() {
////        // given
////        every { cliReader.getValidUserInput(any(), any(), any()) } returns "Title"
////        every { cliReader.getUserInput("Enter project description: ") } returns "Description"
////
////        // when
////        projectsView.createProject()
////
////        // then
////        verify { projectUseCases.createProject("Title", "Description") }
////    }
//}