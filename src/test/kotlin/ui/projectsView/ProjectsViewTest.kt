package ui.projectsView

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.entities.Project
import logic.entities.User
import logic.exceptions.NoLoggedInUserIsSavedInCacheException
import logic.repositories.CacheDataRepository
import logic.useCases.ProjectUseCases
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import ui.projectView.ProjectView
import kotlin.test.Test

class ProjectsViewTest {

    private val cliPrinter: CLIPrinter = mockk(relaxed = true)
    private val cliReader: CLIReader = mockk()
    private val projectUseCases: ProjectUseCases = mockk(relaxed = true)
    private val cacheDataRepository: CacheDataRepository = mockk()
    private val projectView: ProjectView = mockk(relaxed = true)
    private val projectsView = ProjectsView(
        cliPrinter,
        cliReader,
        projectUseCases,
        cacheDataRepository,
        projectView
    )

    @Test
    fun shouldPrintErrorAndExitWhenNoUserLoggedIn() {
        // given
        every { cacheDataRepository.getLoggedInUser() } throws NoLoggedInUserIsSavedInCacheException()

        // when
        projectsView.start()

        // then
        verify { cliPrinter.cliPrintLn("Error: No user logged in. Please log in first.") }
    }

    @Test
    fun shouldCallHandleProjectsViewWhenUserLoggedIn() {
        // given
        val user = mockk<User>()
        every { cacheDataRepository.getLoggedInUser() } returns user
        every { user.type } returns User.Type.MATE
        every { projectUseCases.getAllProjects() } returns emptyList()
        every { cliReader.getUserInput("Choice: ") } returns "back"
        every { cliReader.getValidUserInput(any(), any(), any()) } returns "0"

        // when
        projectsView.start()

        // then
        verify { cliPrinter.printHeader("Projects Menu") }
    }

    @Test
    fun shouldLoopUntilMenuInputReturnsFalse() {
        // given
        val user = mockk<User>()
        every { user.type } returns User.Type.MATE
        every { projectUseCases.getAllProjects() } returns emptyList()
        every { cliReader.getUserInput("Choice: ") } returns "back"
        every { cliReader.getValidUserInput(any(), any(), any()) } returnsMany listOf("1", "0")

        // when
        projectsView.handleProjectsView(user)

        // then
        verify { cliReader.getValidUserInput(any(), eq("Choose an option: "), any()) }
    }

    @Test
    fun shouldPrintMenuOptions() {
        // when
        projectsView.printProjectsMenu()

        // then
        verify { cliPrinter.cliPrintLn("0. Back to main menu") }
    }

    @Test
    fun shouldPrintNoProjectsMessageWhenNoProjects() {
        // given
        every { projectUseCases.getAllProjects() } returns emptyList()

        // when
        projectsView.displayProjects()

        // then
        verify { cliPrinter.cliPrintLn("No projects found.") }
    }

    @Test
    fun shouldPrintProjectsWhenProjectsExist() {
        // given
        val project =
            Project(title = "Test Project", description = "Description", tasks = emptyList(), states = emptyList())
        every { projectUseCases.getAllProjects() } returns listOf(project)
        every { cliPrinter.getThinHorizontal() } returns "----"

        // when
        projectsView.displayProjects()

        // then
        verify { cliPrinter.cliPrintLn("Title: Test Project") }
    }

    @Test
    fun shouldExitWhenBackInput() {
        // given
        val user = mockk<User>()
        every { user.type } returns User.Type.MATE
        every { projectUseCases.getAllProjects() } returns emptyList()
        every { cliReader.getUserInput("Choice: ") } returns "back"

        // when
        projectsView.handleProjectSelection(user)

        // then
        verify { cliReader.getUserInput("Choice: ") }
    }

    @Test
    fun shouldCallCreateProjectAndShowUpdatedProjectsForAdminWithNewInput() {
        // given
        val user = mockk<User>()
        val project = Project(title = "New Project", description = "Desc", tasks = emptyList(), states = emptyList())
        every { user.type } returns User.Type.ADMIN
        every { projectUseCases.getAllProjects() } returnsMany listOf(emptyList(), listOf(project))
        every { cliReader.getUserInput("Choice: ") } returnsMany listOf("new", "back")
        every { cliReader.getValidUserInput(any(), any(), any()) } returns "Title"
        every { cliReader.getUserInput("Enter project description: ") } returns "Description"
        every { cliPrinter.getThinHorizontal() } returns "----"

        // when
        projectsView.handleProjectSelection(user)

        // then
        verify { cliPrinter.cliPrintLn("Title: New Project") }
    }

    @Test
    fun shouldPrintErrorAndPromptAgainForNonAdminWithNewInput() {
        // given
        val user = mockk<User>()
        every { user.type } returns User.Type.MATE
        every { projectUseCases.getAllProjects() } returns emptyList()
        every { cliReader.getUserInput("Choice: ") } returnsMany listOf("new", "back")

        // when
        projectsView.handleProjectSelection(user)

        // then
        verify { cliPrinter.cliPrintLn("Invalid option. Please enter a project number or 'back'.") }
    }

    @Test
    fun shouldStartProjectViewAndPromptAgainWithValidProjectNumber() {
        // given
        val user = mockk<User>()
        val project =
            Project(title = "Test Project", description = "Description", tasks = emptyList(), states = emptyList())
        every { user.type } returns User.Type.MATE
        every { projectUseCases.getAllProjects() } returns listOf(project)
        every { cliReader.getUserInput("Choice: ") } returnsMany listOf("1", "back")
        every { cliPrinter.getThinHorizontal() } returns "----"

        // when
        projectsView.handleProjectSelection(user)

        // then
        verify { projectView.start(project) }
    }

    @Test
    fun shouldPrintErrorAndPromptAgainWithInvalidProjectNumber() {
        // given
        val user = mockk<User>()
        val project =
            Project(title = "Test Project", description = "Description", tasks = emptyList(), states = emptyList())
        every { user.type } returns User.Type.MATE
        every { projectUseCases.getAllProjects() } returns listOf(project)
        every { cliReader.getUserInput("Choice: ") } returnsMany listOf("2", "back")

        // when
        projectsView.handleProjectSelection(user)

        // then
        verify { cliPrinter.cliPrintLn("Invalid project number. Please enter a number between 1 and 1, or 'back'.") }
    }

    @Test
    fun shouldPrintErrorAndPromptAgainWithZeroProjectNumber() {
        // given
        val user = mockk<User>()
        val project =
            Project(title = "Test Project", description = "Description", tasks = emptyList(), states = emptyList())
        every { user.type } returns User.Type.MATE
        every { projectUseCases.getAllProjects() } returns listOf(project)
        every { cliReader.getUserInput("Choice: ") } returnsMany listOf("0", "back")

        // when
        projectsView.handleProjectSelection(user)

        // then
        verify { cliPrinter.cliPrintLn("Invalid project number. Please enter a number between 1 and 1, or 'back'.") }
    }

    @Test
    fun shouldPrintErrorAndPromptAgainWithNonNumericInput() {
        // given
        val user = mockk<User>()
        val project =
            Project(title = "Test Project", description = "Description", tasks = emptyList(), states = emptyList())
        every { user.type } returns User.Type.MATE
        every { projectUseCases.getAllProjects() } returns listOf(project)
        every { cliReader.getUserInput("Choice: ") } returnsMany listOf("abc", "back")

        // when
        projectsView.handleProjectSelection(user)

        // then
        verify { cliPrinter.cliPrintLn("Invalid project number. Please enter a number between 1 and 1, or 'back'.") }
    }

    @Test
    fun shouldCallCreateProjectForAdmin() {
        // given
        val user = mockk<User>()
        every { user.type } returns User.Type.ADMIN
        every { cliReader.getValidUserInput(any(), any(), any()) } returns "Title"
        every { cliReader.getUserInput("Enter project description: ") } returns "Description"

        // when
        projectsView.handleNewProject(user)

        // then
        verify { projectUseCases.createProject("Title", "Description") }
    }

    @Test
    fun shouldPrintErrorForNonAdmin() {
        // given
        val user = mockk<User>()
        every { user.type } returns User.Type.MATE

        // when
        projectsView.handleNewProject(user)

        // then
        verify { cliPrinter.cliPrintLn("Invalid option. Please enter a project number or 'back'.") }
    }

    @Test
    fun shouldStartProjectViewWithValidProjectNumber() {
        // given
        val project =
            Project(title = "Test Project", description = "Description", tasks = emptyList(), states = emptyList())
        val projects = listOf(project)

        // when
        projectsView.handleProjectSelectionInput("1", projects)

        // then
        verify { projectView.start(project) }
    }

    @Test
    fun shouldPrintErrorWithInvalidProjectNumber() {
        // given
        val project =
            Project(title = "Test Project", description = "Description", tasks = emptyList(), states = emptyList())
        val projects = listOf(project)

        // when
        projectsView.handleProjectSelectionInput("2", projects)

        // then
        verify { cliPrinter.cliPrintLn("Invalid project number. Please enter a number between 1 and 1, or 'back'.") }
    }

    @Test
    fun shouldPrintErrorWithZeroProjectNumber() {
        // given
        val project =
            Project(title = "Test Project", description = "Description", tasks = emptyList(), states = emptyList())
        val projects = listOf(project)

        // when
        projectsView.handleProjectSelectionInput("0", projects)

        // then
        verify { cliPrinter.cliPrintLn("Invalid project number. Please enter a number between 1 and 1, or 'back'.") }
    }

    @Test
    fun shouldPrintErrorWithNonNumericInput() {
        // given
        val project =
            Project(title = "Test Project", description = "Description", tasks = emptyList(), states = emptyList())
        val projects = listOf(project)

        // when
        projectsView.handleProjectSelectionInput("abc", projects)

        // then
        verify { cliPrinter.cliPrintLn("Invalid project number. Please enter a number between 1 and 1, or 'back'.") }
    }

    @Test
    fun shouldCreateProjectWithValidInput() {
        // given
        every { cliReader.getValidUserInput(any(), any(), any()) } returns "Title"
        every { cliReader.getUserInput("Enter project description: ") } returns "Description"

        // when
        projectsView.createProject()

        // then
        verify { projectUseCases.createProject("Title", "Description") }
    }
}