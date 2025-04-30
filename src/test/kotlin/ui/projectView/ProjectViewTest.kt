package ui.projectView

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.entities.Project
import logic.entities.State
import logic.entities.Task
import logic.entities.User
import logic.repositories.CacheDataRepository
import logic.useCases.ProjectUseCases
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import java.util.UUID

class ProjectViewTest {

    private lateinit var cliPrinter: CLIPrinter
    private lateinit var cliReader: CLIReader
    private lateinit var projectUseCases: ProjectUseCases

    private lateinit var cacheDataRepository: CacheDataRepository
    private lateinit var projectView: ProjectView
    private lateinit var project: Project

    @BeforeEach
    fun setUp() {
        cliPrinter = mockk(relaxed = true)
        cliReader = mockk()
        projectUseCases = mockk()

        cacheDataRepository = mockk()
        project = mockk()
        projectView = ProjectView(
            cliPrinter,
            cliReader,
            projectUseCases,
            cacheDataRepository
        )

        every { project.id } returns UUID.randomUUID()
        every { project.title } returns "Test Project"
        every { project.description } returns "Test Description"
        every { project.tasks } returns emptyList()
        every { project.states } returns emptyList()


    }

    @Test
    fun `should print error message when no user is logged in`() {
        // Given
        every { cacheDataRepository.getLoggedInUser() } returns null

        // When
        projectView.start(project)

        // Then
        verify { cliPrinter.cliPrintLn(ProjectView.ERROR_MESSAGE) }
    }

    @Test
    fun `should not display swimlanes when no user is logged in`() {
        // Given
        every { cacheDataRepository.getLoggedInUser() } returns null

        // When
        projectView.start(project)

        // Then
        verify(exactly = 0) { cliPrinter.printHeader(any()) }
    }

    @Test
    fun `should display swimlanes header when user is logged in`() {
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
    fun `should display no states message when states are empty`() {
        // Given
        val user = mockk<User> {
            every { type } returns User.Type.MATE
        }
        every { cacheDataRepository.getLoggedInUser() } returns user
        every { cliReader.getValidUserInput(any(), any(), any()) } returns "0"

        // When
        projectView.start(project)

        // Then
        verify { cliPrinter.cliPrintLn("No states defined for this project.") }
    }

    @Test
    fun `should display project menu header when user is logged in`() {
        // Given
        val user = mockk<User> {
            every { type } returns User.Type.MATE
        }
        every { cacheDataRepository.getLoggedInUser() } returns user
        every { cliReader.getValidUserInput(any(), any(), any()) } returns "0"

        // When
        projectView.start(project)

        // Then
        verify { cliPrinter.printHeader("Project: Test Project") }
    }

    @Test
    fun `should prompt for user input when user is logged in`() {
        // Given
        val user = mockk<User> {
            every { type } returns User.Type.MATE
        }
        every { cacheDataRepository.getLoggedInUser() } returns user
        every { cliReader.getValidUserInput(any(), any(), any()) } returns "0"

        // When
        projectView.start(project)

        // Then
        verify { cliReader.getValidUserInput(any(), eq("Choose an option: "), any()) }
    }

    @Test
    fun `should print option 1 in menu for non-admin user`() {
        // Given
        projectView.currentProject = project
        val user = mockk<User> {
            every { type } returns User.Type.MATE
        }
        every { cacheDataRepository.getLoggedInUser() } returns user

        // When
        projectView.javaClass.getDeclaredMethod("printProjectMenu").apply {
            isAccessible = true
            invoke(projectView)
        }

        // Then
        verify { cliPrinter.cliPrintLn("1. Add new task") }
    }

    @Test
    fun `should print option 2 in menu for non-admin user`() {
        // Given
        projectView.currentProject = project
        val user = mockk<User> {
            every { type } returns User.Type.MATE
        }
        every { cacheDataRepository.getLoggedInUser() } returns user

        // When
        projectView.javaClass.getDeclaredMethod("printProjectMenu").apply {
            isAccessible = true
            invoke(projectView)
        }

        // Then
        verify { cliPrinter.cliPrintLn("2. Select task") }
    }

    @Test
    fun `should print option 3 in menu for non-admin user`() {
        // Given
        projectView.currentProject = project
        val user = mockk<User> {
            every { type } returns User.Type.MATE
        }
        every { cacheDataRepository.getLoggedInUser() } returns user

        // When
        projectView.javaClass.getDeclaredMethod("printProjectMenu").apply {
            isAccessible = true
            invoke(projectView)
        }

        // Then
        verify { cliPrinter.cliPrintLn("3. View project logs") }
    }

    @Test
    fun `should print option 0 in menu for non-admin user`() {
        // Given
        projectView.currentProject = project
        val user = mockk<User> {
            every { type } returns User.Type.MATE
        }
        every { cacheDataRepository.getLoggedInUser() } returns user

        // When
        projectView.javaClass.getDeclaredMethod("printProjectMenu").apply {
            isAccessible = true
            invoke(projectView)
        }

        // Then
        verify { cliPrinter.cliPrintLn("0. Back to projects") }
    }

    @Test
    fun `should not print option 4 for non-admin user`() {
        // Given
        projectView.currentProject = project
        val user = mockk<User> {
            every { type } returns User.Type.MATE
        }
        every { cacheDataRepository.getLoggedInUser() } returns user

        // When
        projectView.javaClass.getDeclaredMethod("printProjectMenu").apply {
            isAccessible = true
            invoke(projectView)
        }

        // Then
        verify(exactly = 0) { cliPrinter.cliPrintLn("4. Edit project") }
    }

    @Test
    fun `should not print option 5 for non-admin user`() {
        // Given
        projectView.currentProject = project
        val user = mockk<User> {
            every { type } returns User.Type.MATE
        }
        every { cacheDataRepository.getLoggedInUser() } returns user

        // When
        projectView.javaClass.getDeclaredMethod("printProjectMenu").apply {
            isAccessible = true
            invoke(projectView)
        }

        // Then
        verify(exactly = 0) { cliPrinter.cliPrintLn("5. Delete project") }
    }

    @Test
    fun `should print option 4 for admin user`() {
        // Given
        projectView.currentProject = project
        val user = mockk<User> {
            every { type } returns User.Type.ADMIN
        }
        every { cacheDataRepository.getLoggedInUser() } returns user

        // When
        projectView.javaClass.getDeclaredMethod("printProjectMenu").apply {
            isAccessible = true
            invoke(projectView)
        }

        // Then
        verify { cliPrinter.cliPrintLn("4. Edit project") }
    }

    @Test
    fun `should print option 5 for admin user`() {
        // Given
        projectView.currentProject = project
        val user = mockk<User> {
            every { type } returns User.Type.ADMIN
        }
        every { cacheDataRepository.getLoggedInUser() } returns user

        // When
        projectView.javaClass.getDeclaredMethod("printProjectMenu").apply {
            isAccessible = true
            invoke(projectView)
        }

        // Then
        verify { cliPrinter.cliPrintLn("5. Delete project") }
    }

    @Test
    fun `should return immediately when non-admin selects option 4`() {
        // Given
        val user = mockk<User> {
            every { type } returns User.Type.MATE
        }
        every { cacheDataRepository.getLoggedInUser() } returns user
        every { cliReader.getValidUserInput(any(), any(), any()) } returns "4"

        // When
        projectView.javaClass.getDeclaredMethod("handleUserInput").apply {
            isAccessible = true
            invoke(projectView)
        }

        // Then
        verify { cliReader.getValidUserInput(any(), eq("Choose an option: "), any()) }
    }

    @Test
    fun `should enter edit project menu when admin selects option 4`() {
        // Given
        val user = mockk<User> {
            every { type } returns User.Type.ADMIN
        }
        every { cacheDataRepository.getLoggedInUser() } returns user
        every { cliReader.getValidUserInput(any(), any(), any()) } returnsMany listOf("4", "0")

        // When
        projectView.start(project)

        // Then
        verify { cliPrinter.printHeader("Edit Project") }
    }

//    @Test
//    fun `should delete project when admin selects option 5`() {
//        // Given
//        projectView.currentProject = project
//        val user = mockk<User> {
//            every { type } returns User.Type.ADMIN
//        }
//        every { cacheDataRepository.getLoggedInUser() } returns user
//        every { cliReader.getValidUserInput(any(), any(), any()) } returns "5"
//        every { projectUseCases.deleteProject(project.id) } returns Unit
//
//        // When
//        projectView.javaClass.getDeclaredMethod("handleUserInput").apply {
//            isAccessible = true
//            invoke(projectView)
//        }
//
//        // Then
//        verify { projectUseCases.deleteProject(project.id) }
//    }

//    @Test
//    fun `should print project deleted message when admin selects option 5`() {
//        // Given
//        projectView.currentProject=project
//        val user = mockk<User> {
//            every { type } returns User.Type.ADMIN
//        }
//        every { cacheDataRepository.getLoggedInUser() } returns user
//        every { cliReader.getValidUserInput(any(), any(), any()) } returns "5"
//        every { projectUseCases.deleteProject(project.id) } returns Unit
//
//        // When
//        projectView.javaClass.getDeclaredMethod("handleUserInput").apply {
//            isAccessible = true
//            invoke(projectView)
//        }
//
//        // Then
//        verify { cliPrinter.cliPrintLn("Project deleted.") }
//    }

    @Test
    fun `should display state headers in swimlanes with states and tasks`() {
        // Given
        val state1 = mockk<State> {
            every { id } returns UUID.randomUUID()
            every { title } returns "TODO"
        }
        val state2 = mockk<State> {
            every { id } returns UUID.randomUUID()
            every { title } returns "Done"
        }
        val task1 = mockk<Task> {
            every { title } returns "Task 1"
            every { state } returns state1
        }
        val task2 = mockk<Task> {
            every { title } returns "Task 2"
            every { state } returns state2
        }
        every { project.states } returns listOf(state1, state2)
        every { project.tasks } returns listOf(task1, task2)
        every { project.title } returns "Test Project"

        // When
        projectView.javaClass.getDeclaredMethod("displaySwimlanes", Project::class.java).apply {
            isAccessible = true
            invoke(projectView, project)
        }

        // Then
        verify { cliPrinter.cliPrint(" TODO                           |") }
    }

    @Test
    fun `should display task in swimlanes with states and tasks`() {
        // Given
        val state1 = mockk<State> {
            every { id } returns UUID.randomUUID()
            every { title } returns "TODO"
        }
        val state2 = mockk<State> {
            every { id } returns UUID.randomUUID()
            every { title } returns "Done"
        }
        val task1 = mockk<Task> {
            every { title } returns "Task 1"
            every { state } returns state1
        }
        val task2 = mockk<Task> {
            every { title } returns "Task 2"
            every { state } returns state2
        }
        every { project.states } returns listOf(state1, state2)
        every { project.tasks } returns listOf(task1, task2)
        every { project.title } returns "Test Project"

        // When
        projectView.javaClass.getDeclaredMethod("displaySwimlanes", Project::class.java).apply {
            isAccessible = true
            invoke(projectView, project)
        }

        // Then
        verify { cliPrinter.cliPrint(" Task 1                         |") }
    }

    @Test
    fun `should display empty column in swimlanes with states and tasks`() {
        // Given
        val state1 = mockk<State> {
            every { id } returns UUID.randomUUID()
            every { title } returns "TODO"
        }
        val state2 = mockk<State> {
            every { id } returns UUID.randomUUID()
            every { title } returns "Done"
        }
        val task1 = mockk<Task> {
            every { title } returns "Task 1"
            every { state } returns state1
        }
        val task2 = mockk<Task> {
            every { title } returns "Task 2"
            every { state } returns state2
        }
        every { project.states } returns listOf(state1, state2)
        every { project.tasks } returns listOf(task1, task2)
        every { project.title } returns "Test Project"

        // When
        projectView.javaClass.getDeclaredMethod("displaySwimlanes", Project::class.java).apply {
            isAccessible = true
            invoke(projectView, project)
        }

        // Then
        verify { cliPrinter.cliPrint("".padEnd(30) + "|") }
    }

    @Test
    fun `should update project title when admin selects edit title`() {
        // Given
        val user = mockk<User> {
            every { type } returns User.Type.ADMIN
        }
        every { cacheDataRepository.getLoggedInUser() } returns user
        every { cliReader.getValidUserInput(any(), eq("Choose an option: "), any()) } returns "4"
        every { cliReader.getValidUserInput(any(), eq("Enter new project title: "), any()) } returns "New Title"
        every { cliReader.getValidUserInput(any(), eq("Choose an option: "), any()) } returns "1"
        every { projectUseCases.editProjectTitle(project.id, "New Title") } returns Unit

        // When
        projectView.javaClass.getDeclaredMethod("editProject").apply {
            isAccessible = true
            invoke(projectView)
        }

        // Then
        verify { projectUseCases.editProjectTitle(project.id, "New Title") }
    }

    @Test
    fun `should print title updated message when admin selects edit title`() {
        // Given
        val user = mockk<User> {
            every { type } returns User.Type.ADMIN
        }
        every { cacheDataRepository.getLoggedInUser() } returns user
        every { cliReader.getValidUserInput(any(), eq("Choose an option: "), any()) } returns "4"
        every { cliReader.getValidUserInput(any(), eq("Enter new project title: "), any()) } returns "New Title"
        every { cliReader.getValidUserInput(any(), eq("Choose an option: "), any()) } returns "1"
        every { projectUseCases.editProjectTitle(project.id, "New Title") } returns Unit

        // When
        projectView.javaClass.getDeclaredMethod("editProject").apply {
            isAccessible = true
            invoke(projectView)
        }

        // Then
        verify { cliPrinter.cliPrintLn("Project title updated.") }
    }

    @Test
    fun `should update project description when admin selects edit description`() {
        // Given
        val user = mockk<User> {
            every { type } returns User.Type.ADMIN
        }
        every { cacheDataRepository.getLoggedInUser() } returns user
        every { cliReader.getValidUserInput(any(), eq("Choose an option: "), any()) } returns "4"
        every {
            cliReader.getValidUserInput(
                any(),
                eq("Enter new project description: "),
                any()
            )
        } returns "New Description"
        every { cliReader.getValidUserInput(any(), eq("Choose an option: "), any()) } returns "2"
        every { projectUseCases.editProjectDescription(project.id, "New Description") } returns Unit

        // When
        projectView.javaClass.getDeclaredMethod("editProject").apply {
            isAccessible = true
            invoke(projectView)
        }

        // Then
        verify { projectUseCases.editProjectDescription(project.id, "New Description") }
    }

    @Test
    fun `should print description updated message when admin selects edit description`() {
        // Given
        val user = mockk<User> {
            every { type } returns User.Type.ADMIN
        }
        every { cacheDataRepository.getLoggedInUser() } returns user
        every { cliReader.getValidUserInput(any(), eq("Choose an option: "), any()) } returns "4"
        every {
            cliReader.getValidUserInput(
                any(),
                eq("Enter new project description: "),
                any()
            )
        } returns "New Description"
        every { cliReader.getValidUserInput(any(), eq("Choose an option: "), any()) } returns "2"
        every { projectUseCases.editProjectDescription(project.id, "New Description") } returns Unit

        // When
        projectView.javaClass.getDeclaredMethod("editProject").apply {
            isAccessible = true
            invoke(projectView)
        }

        // Then
        verify { cliPrinter.cliPrintLn("Project description updated.") }
    }

    @Test
    fun `should prompt for input in edit project menu`() {
        // Given
        val user = mockk<User> {
            every { type } returns User.Type.ADMIN
        }
        every { cacheDataRepository.getLoggedInUser() } returns user
        every { cliReader.getValidUserInput(any(), eq("Choose an option: "), any()) } returns "0"

        // When
        projectView.javaClass.getDeclaredMethod("editProject").apply {
            isAccessible = true
            invoke(projectView)
        }

        // Then
        verify { cliReader.getValidUserInput(any(), eq("Choose an option: "), any()) }
    }

    @Test
    fun `should handle duplicate string utility function`() {
        // Given
        val string = "-"
        val numberOfDuplication = 30

        // When
        val result = projectView.javaClass.getDeclaredMethod("duplicate", String::class.java, Int::class.java).apply {
            isAccessible = true
            invoke(projectView, string, numberOfDuplication) as String
        }

        // Then
        assert(result.equals("-".repeat(30)))
    }
}