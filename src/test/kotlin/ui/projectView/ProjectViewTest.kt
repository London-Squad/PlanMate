package ui.projectView

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.entities.Project
import logic.entities.User
import logic.repositories.CacheDataRepository
import logic.useCases.ProjectUseCases
import main.logic.useCases.LogUseCases
import main.logic.useCases.StateUseCases
import main.logic.useCases.TaskUseCases
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader

class ProjectViewTest {

    private lateinit var cliPrinter: CLIPrinter
    private lateinit var cliReader: CLIReader
    private lateinit var projectUseCases: ProjectUseCases
    private lateinit var taskUseCases: TaskUseCases
    private lateinit var stateUseCases: StateUseCases
    private lateinit var logUseCases: LogUseCases
    private lateinit var cacheDataRepository: CacheDataRepository
    private lateinit var projectView: ProjectView
    private lateinit var project: Project

    @BeforeEach
    fun setUp() {
        cliPrinter = mockk(relaxed = true)
        cliReader = mockk()
        projectUseCases = mockk()
        taskUseCases = mockk()
        stateUseCases = mockk()
        logUseCases = mockk()
        cacheDataRepository = mockk()
        project = mockk()
        projectView = ProjectView(
            cliPrinter,
            cliReader,
            projectUseCases,
            taskUseCases,
            stateUseCases,
            logUseCases,
            cacheDataRepository
        )

        every { project.id } returns java.util.UUID.randomUUID()
        every { project.title } returns "Test Project"
        every { project.description } returns "Test Description"
        every { project.tasks } returns emptyList()
        every { project.states } returns emptyList()
    }

    @Test
    fun `start exits when no user is logged in`() {
        // Given
        every { cacheDataRepository.getLoggedInUser() } returns null

        // When
        projectView.start(project)

        // Then
        verify { cliPrinter.cliPrintLn(ProjectView.ERROR_MESSAGE) }
    }

    @Test
    fun `start displays swimlanes and menu when user is logged in`() {
        // Given
        val user = mockk<User> {
            every { type } returns User.Type.MATE
        }
        every { cacheDataRepository.getLoggedInUser() } returns user
        every { cliReader.getValidUserInput(any(), any(), any()) } returns "0"

        // When
        projectView.start(project)

        // Then
        verify { cliPrinter.printHeader("Swimlanes: Test Project") }
    }

    @Test
    fun `printProjectMenu shows all options for admin user`() {
        // Given
        projectView.currentProject = project
        val user = mockk<User> {
            every { type } returns User.Type.ADMIN
        }
        every { cacheDataRepository.getLoggedInUser() } returns user
        every { project.title } returns "Test Project"

        // When
        projectView.javaClass.getDeclaredMethod("printProjectMenu").apply {
            isAccessible = true
            invoke(projectView)
        }

        // Then
        verify { cliPrinter.cliPrintLn("5. Delete project") }
    }

    @Test
    fun `printProjectMenu hides admin options for non-admin user`() {
        // Given
        projectView.currentProject = project
        val user = mockk<User> {
            every { type } returns User.Type.MATE
        }
        every { cacheDataRepository.getLoggedInUser() } returns user
        every { project.title } returns "Test Project"

        // When
        projectView.javaClass.getDeclaredMethod("printProjectMenu").apply {
            isAccessible = true
            invoke(projectView)
        }

        // Then
        verify(exactly = 0) { cliPrinter.cliPrintLn("4. Edit project") }
    }


    @Test
    fun `handleUserInput returns on option 0`() {
        // Given
        val user = mockk<User> {
            every { type } returns User.Type.MATE
        }
        every { cacheDataRepository.getLoggedInUser() } returns user
        every { cliReader.getValidUserInput(any(), any(), any()) } returns "0"

        // When
        projectView.javaClass.getDeclaredMethod("handleUserInput").apply {
            isAccessible = true
            invoke(projectView)
        }

        // Then
        verify { cliReader.getValidUserInput(any(), any(), any()) }
    }

    @Test
    fun `displaySwimlanes shows message when states are empty`() {
        // Given
        every { project.states } returns emptyList()
        every { project.title } returns "Test Project"

        // When
        projectView.javaClass.getDeclaredMethod("displaySwimlanes", Project::class.java).apply {
            isAccessible = true
            invoke(projectView, project)
        }

        // Then
        verify { cliPrinter.cliPrintLn("No states defined for this project.") }
    }


    @Test
    fun `editProject returns on option 0`() {
        // Given
        every { cliReader.getValidUserInput(any(), any(), any()) } returns "0"

        // When
        projectView.javaClass.getDeclaredMethod("editProject").apply {
            isAccessible = true
            invoke(projectView)
        }

        // Then
        verify { cliReader.getValidUserInput(any(), any(), any()) }
    }


    @Test
    fun `deleteProject deletes project`() {
        // Given
        every { projectUseCases.deleteProject(project.id) } returns Unit

        // When
        projectView.javaClass.getDeclaredMethod("deleteProject", Project::class.java).apply {
            isAccessible = true
            invoke(projectView, project)
        }

        // Then
        verify { projectUseCases.deleteProject(project.id) }
    }
}