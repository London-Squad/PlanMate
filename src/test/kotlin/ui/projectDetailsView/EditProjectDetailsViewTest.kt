package ui.projectDetailsView

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.entities.Project
import logic.useCases.ProjectUseCases
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import ui.statesView.StatesView
import java.util.UUID

class EditProjectDetailsViewTest {

    private lateinit var cliPrinter: CLIPrinter
    private lateinit var cliReader: CLIReader
    private lateinit var projectUseCases: ProjectUseCases
    private lateinit var editProjectView: EditProjectView
    private lateinit var statesView: StatesView
    private lateinit var project: Project

    @BeforeEach
    fun setUp() {
        cliPrinter = mockk(relaxed = true)
        cliReader = mockk()
        projectUseCases = mockk()
        project = mockk()
        statesView = mockk()
        editProjectView = EditProjectView(cliPrinter, cliReader, projectUseCases, statesView)

        every { project.id } returns UUID.randomUUID()
        every { project.title } returns "Test Project"
        every { project.description } returns "Test Description"
        every { project.copy(title = any()) } returns mockk()
        every { project.copy(description = any()) } returns mockk()
    }

    @Test
    fun `should process option 1 when user selects edit title`() {
        // Given
        every { cliReader.getValidUserInput(any(), any(), any()) } returnsMany listOf("1", "New Title")
        every { projectUseCases.editProjectTitle(project.id, "New Title") } returns Unit
        every { project.copy(title = "New Title") } returns project

        // When
        editProjectView.editProject(project)

        // Then
        verify { projectUseCases.editProjectTitle(project.id, "New Title") }
    }

    @Test
    fun `should process option 2 when user selects edit description`() {
        // Given
        every { cliReader.getValidUserInput(any(), any(), any()) } returnsMany listOf("2", "New Description")
        every { projectUseCases.editProjectDescription(project.id, "New Description") } returns Unit
        every { project.copy(description = "New Description") } returns project

        // When
        editProjectView.editProject(project)

        // Then
        verify { projectUseCases.editProjectDescription(project.id, "New Description") }
    }

    @Test
    fun `should return when user selects option 0`() {
        // Given
        every { cliReader.getValidUserInput(any(), any(), any()) } returns "0"

        // When
        editProjectView.editProject(project)

        // Then
        verify { cliReader.getValidUserInput(any(), any(), any()) }
    }

    @Test
    fun `should update project title when editing title`() {
        // Given
        editProjectView.currentProject = project
        every { cliReader.getValidUserInput(any(), any(), any()) } returns "New Title"
        every { projectUseCases.editProjectTitle(project.id, "New Title") } returns Unit

        val updatedProject = mockk<Project>()
        every { updatedProject.title } returns "New Title"
        every { updatedProject.id } returns project.id
        every { updatedProject.copy(title = "New Title") } returns updatedProject
        every { project.copy(title = "New Title") } returns updatedProject

        // When
        editProjectView.javaClass.getDeclaredMethod("editProjectTitle").apply {
            isAccessible = true
            invoke(editProjectView)
        }

        // Then
        verify { projectUseCases.editProjectTitle(project.id, "New Title") }
    }

    @Test
    fun `should print confirmation message when title is updated`() {
        // Given
        editProjectView.currentProject = project
        every { cliReader.getValidUserInput(any(), any(), any()) } returns "New Title"
        every { projectUseCases.editProjectTitle(project.id, "New Title") } returns Unit

        val updatedProject = mockk<Project>()
        every { updatedProject.title } returns "New Title"
        every { updatedProject.id } returns project.id
        every { updatedProject.copy(title = "New Title") } returns updatedProject
        every { project.copy(title = "New Title") } returns updatedProject

        // When
        editProjectView.javaClass.getDeclaredMethod("editProjectTitle").apply {
            isAccessible = true
            invoke(editProjectView)
        }

        // Then
        verify { cliPrinter.cliPrintLn("Project title updated.") }
    }

    @Test
    fun `should update project description when editing description`() {
        // Given
        editProjectView.currentProject = project
        every { cliReader.getValidUserInput(any(), any(), any()) } returns "New Description"
        every { projectUseCases.editProjectDescription(project.id, "New Description") } returns Unit
        every { project.copy(description = "New Description") } returns mockk()

        // When
        editProjectView.javaClass.getDeclaredMethod("editProjectDescription").apply {
            isAccessible = true
            invoke(editProjectView)
        }

        // Then
        verify { projectUseCases.editProjectDescription(project.id, "New Description") }
    }

    @Test
    fun `should print confirmation message when description is updated`() {
        // Given
        editProjectView.currentProject = project
        every { cliReader.getValidUserInput(any(), any(), any()) } returns "New Description"
        every { projectUseCases.editProjectDescription(project.id, "New Description") } returns Unit
        every { project.copy(description = "New Description") } returns mockk()

        // When
        editProjectView.javaClass.getDeclaredMethod("editProjectDescription").apply {
            isAccessible = true
            invoke(editProjectView)
        }

        // Then
        verify { cliPrinter.cliPrintLn("Project description updated.") }
    }
}