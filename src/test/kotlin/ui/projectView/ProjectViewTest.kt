package ui.projectView

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.entities.Project
import logic.entities.User
import logic.exceptions.NoLoggedInUserIsSavedInCacheException
import logic.repositories.CacheDataRepository
import logic.useCases.ProjectUseCases
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import java.util.*
import kotlin.test.assertEquals

class ProjectViewTest {

    private lateinit var cliPrinter: CLIPrinter
    private lateinit var cliReader: CLIReader
    private lateinit var cacheDataRepository: CacheDataRepository
    private lateinit var swimlanesView: SwimlanesView
    private lateinit var editProjectView: EditProjectView
    private lateinit var deleteProjectView: DeleteProjectView
    private lateinit var projectTasksView: ProjectTasksView
    private lateinit var projectUseCases: ProjectUseCases
    private lateinit var projectView: ProjectView
    private lateinit var project: Project
    private lateinit var user: User

    @BeforeEach
    fun setUp() {
        cliPrinter = mockk(relaxed = true)
        cliReader = mockk()
        cacheDataRepository = mockk()
        swimlanesView = mockk()
        editProjectView = mockk()
        deleteProjectView = mockk()
        projectTasksView = mockk()
        projectUseCases = mockk(relaxed = true)
        project = mockk()
        user = mockk()

        projectView = ProjectView(
            cliPrinter,
            cliReader,
            cacheDataRepository,
            swimlanesView,
            editProjectView,
            deleteProjectView,
            projectTasksView,
            projectUseCases
        )

        every { project.title } returns "Test Project"
        every { project.id } returns UUID.randomUUID()

        every { cacheDataRepository.getLoggedInUser() } returns user
        every { user.type } returns User.Type.ADMIN

        every { swimlanesView.displaySwimlanes(project) } returns Unit
        every { projectTasksView.manageTasks(project) } returns project
        every { editProjectView.editProject(project) } returns project
    }

    @Test
    fun `should display error message when user is not logged in`() {
        // Given
        every { cacheDataRepository.getLoggedInUser() } throws NoLoggedInUserIsSavedInCacheException()
        every { cliReader.getValidUserInput(any(), any(), any()) } returns "0"

        // When
        projectView.start(project)

        // Then
        verify { cliPrinter.cliPrintLn(ProjectView.ERROR_MESSAGE) }
    }

    @Test
    fun `should display swimlanes when user is logged in`() {
        // Given
        every { cliReader.getValidUserInput(any(), any(), any()) } returns sequenceOf("0").iterator().asSequence().toString()

        // When
        projectView.start(project)

        // Then
        verify { swimlanesView.displaySwimlanes(project) }
    }

    @Test
    fun `should print basic menu options for non-admin user`() {
        // Given
        every { user.type } returns User.Type.MATE
        every { cliReader.getValidUserInput(any(), any(), any()) } returns sequenceOf("0").iterator().asSequence().toString()

        // When
        projectView.start(project)

        // Then
        verify { cliPrinter.cliPrintLn("1. Manage tasks") }
        verify { cliPrinter.cliPrintLn("2. View project logs") }
        verify { cliPrinter.cliPrintLn("0. Back to projects") }
    }

    @Test
    fun `should print admin options for admin user`() {
        // Given
        every { user.type } returns User.Type.ADMIN
        every { cliReader.getValidUserInput(any(), any(), any()) } returns sequenceOf("0").iterator().asSequence().toString()

        // When
        projectView.start(project)

        // Then
        verify { cliPrinter.cliPrintLn("3. Edit project") }
        verify { cliPrinter.cliPrintLn("4. Delete project") }
    }

    @Test
    fun `should return when non-admin user selects option 0`() {
        // Given
        every { user.type } returns User.Type.MATE
        every { cliReader.getValidUserInput(any(), any(), any()) } returns sequenceOf("0").iterator().asSequence().toString()

        // When
        projectView.start(project)

        // Then
        verify { cliReader.getValidUserInput(any(), any(), any()) }
    }

    @Test
    fun `should call projectTasksView and reload project when non-admin user selects option 1`() {
        // Given
        every { user.type } returns User.Type.MATE
        val inputs = mutableListOf("1", "0")
        every { cliReader.getValidUserInput(any(), any(), any()) } answers {
            inputs.removeAt(0)
        }
        every { projectUseCases.getProjectById(any()) } returns project
        every { project.id } returns UUID.randomUUID()

        // When
        projectView.start(project)

        // Then
        verify { projectTasksView.manageTasks(project) }
        verify { cliPrinter.printHeader("Project: Test Project") }
    }

    @Test
    fun `should return when non-admin user selects option 2`() {
        // Given
        every { user.type } returns User.Type.MATE
        every { cliReader.getValidUserInput(any(), any(), any()) } returns sequenceOf("2", "0").iterator().asSequence().toString()

        // When
        projectView.start(project)

        // Then
        verify { cliReader.getValidUserInput(any(), any(), any()) }
    }

    @Test
    fun `should return when admin user selects option 0`() {
        // Given
        every { user.type } returns User.Type.ADMIN
        every { cliReader.getValidUserInput(any(), any(), any()) } returns sequenceOf("0").iterator().asSequence().toString()

        // When
        projectView.start(project)

        // Then
        verify { cliReader.getValidUserInput(any(), any(), any()) }
    }

    @Test
    fun `should call projectTasksView and reload project when admin user selects option 1`() {
        // Given
        every { user.type } returns User.Type.ADMIN
        val inputs = mutableListOf("1", "0")
        every { cliReader.getValidUserInput(any(), any(), any()) } answers {
            inputs.removeAt(0)
        }
        every { projectUseCases.getProjectById(any()) } returns project
        every { project.id } returns UUID.randomUUID()

        // When
        projectView.start(project)

        // Then
        verify { projectTasksView.manageTasks(project) }
        verify { cliPrinter.printHeader("Project: Test Project") }
    }

    @Test
    fun `should return when admin user selects option 2`() {
        // Given
        every { user.type } returns User.Type.ADMIN
        every { cliReader.getValidUserInput(any(), any(), any()) } returns sequenceOf("2", "0").iterator().asSequence().toString()

        // When
        projectView.start(project)

        // Then
        verify { cliReader.getValidUserInput(any(), any(), any()) }
    }

    @Test
    fun `should call editProjectView and reload project when admin user selects option 3`() {
        // Given
        every { user.type } returns User.Type.ADMIN
        val inputs = mutableListOf("3", "0")
        every { cliReader.getValidUserInput(any(), any(), any()) } answers {
            inputs.removeAt(0)
        }
        every { projectUseCases.getProjectById(any()) } returns project
        every { project.id } returns UUID.randomUUID()
        every { editProjectView.editProject(project) } returns project

        // When
        projectView.start(project)

        // Then
        verify { editProjectView.editProject(project) }
        verify { cliPrinter.printHeader("Project: Test Project") }
    }

    @Test
    fun `should call deleteProjectView when admin user selects option 4`() {
        // Given
        every { user.type } returns User.Type.ADMIN
        every { cliReader.getValidUserInput(any(), any(), any()) } returns "4"
        every { deleteProjectView.deleteProject(project) } returns Unit

        // When
        projectView.start(project)

        // Then
        verify { deleteProjectView.deleteProject(project) }
    }
}