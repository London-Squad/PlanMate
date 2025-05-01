package ui.projectView

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.entities.Project
import logic.useCases.ProjectUseCases
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ui.cliPrintersAndReaders.CLIPrinter
import java.util.UUID

class DeleteProjectViewTest {

    private lateinit var cliPrinter: CLIPrinter
    private lateinit var projectUseCases: ProjectUseCases
    private lateinit var deleteProjectView: DeleteProjectView
    private lateinit var project: Project

    @BeforeEach
    fun setUp() {
        cliPrinter = mockk(relaxed = true)
        projectUseCases = mockk()
        project = mockk()
        deleteProjectView = DeleteProjectView(cliPrinter, projectUseCases)

        // Mock project ID
        every { project.id } returns UUID.randomUUID()
    }

    @Test
    fun `deleteProject calls projectUseCases to delete project`() {
        // Given
        every { projectUseCases.deleteProject(project.id) } returns Unit

        // When
        deleteProjectView.deleteProject(project)

        // Then
        verify { projectUseCases.deleteProject(project.id) }
    }

    @Test
    fun `deleteProject prints confirmation message`() {
        // Given
        every { projectUseCases.deleteProject(project.id) } returns Unit

        // When
        deleteProjectView.deleteProject(project)

        // Then
        verify { cliPrinter.cliPrintLn("Project deleted.") }
    }
}