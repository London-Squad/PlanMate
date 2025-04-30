package logic.useCases

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.entities.Project
import logic.repositories.ProjectsRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID

class ProjectUseCasesTest {

    private lateinit var projectsRepository: ProjectsRepository
    private lateinit var projectUseCases: ProjectUseCases

    @BeforeEach
    fun setUp() {
        projectsRepository = mockk()
        projectUseCases = ProjectUseCases(projectsRepository)
    }

    @Test
    fun `getAllProjects returns empty list when repository has no projects`() {
        // Given
        every { projectsRepository.getAllProjects() } returns emptyList()

        // When
        val projects = projectUseCases.getAllProjects()

        // Then
        assertThat(projects).isEmpty()
    }

    @Test
    fun `getAllProjects returns projects from repository`() {
        // Given
        val project = mockk<Project>()
        every { projectsRepository.getAllProjects() } returns listOf(project)

        // When
        val projects = projectUseCases.getAllProjects()

        // Then
        assertThat(projects).containsExactly(project)
    }

    @Test
    fun `getProjectById returns project when it exists`() {
        // Given
        val projectId = UUID.randomUUID()
        val project = mockk<Project> {
            every { id } returns projectId
        }
        every { projectsRepository.getAllProjects() } returns listOf(project)

        // When
        val result = projectUseCases.getProjectById(projectId)

        // Then
        assertThat(result).isEqualTo(project)
    }

    @Test
    fun `getProjectById returns null when project does not exist`() {
        // Given
        val projectId = UUID.randomUUID()
        every { projectsRepository.getAllProjects() } returns emptyList()

        // When
        val result = projectUseCases.getProjectById(projectId)

        // Then
        assertThat(result).isNull()
    }

    @Test
    fun `createProject adds project with default states to repository`() {
        // Given
        val title = "Test Project"
        val description = "Test Description"
        every { projectsRepository.addNewProject(any()) } returns Unit

        // When
        val project = projectUseCases.createProject(title, description)

        // Then
        assertThat(project.title).isEqualTo(title)
    }

    @Test
    fun `createProject sets three default states with correct titles`() {
        // Given
        val title = "Test Project"
        val description = "Test Description"
        every { projectsRepository.addNewProject(any()) } returns Unit

        // When
        val project = projectUseCases.createProject(title, description)

        // Then
        assertThat(project.states.map { it.title }).containsExactly("TODO", "InProgress", "Done")
    }

    @Test
    fun `editProjectTitle calls repository with correct parameters`() {
        // Given
        val projectId = UUID.randomUUID()
        val newTitle = "New Title"
        every { projectsRepository.editProjectTitle(projectId, newTitle) } returns Unit

        // When
        projectUseCases.editProjectTitle(projectId, newTitle)

        // Then
        verify { projectsRepository.editProjectTitle(projectId, newTitle) }
    }

    @Test
    fun `editProjectDescription calls repository with correct parameters`() {
        // Given
        val projectId = UUID.randomUUID()
        val newDescription = "New Description"
        every { projectsRepository.editProjectDescription(projectId, newDescription) } returns Unit

        // When
        projectUseCases.editProjectDescription(projectId, newDescription)

        // Then
        verify { projectsRepository.editProjectDescription(projectId, newDescription) }
    }

    @Test
    fun `deleteProject calls repository with correct project ID`() {
        // Given
        val projectId = UUID.randomUUID()
        every { projectsRepository.deleteProject(projectId) } returns Unit

        // When
        projectUseCases.deleteProject(projectId)

        // Then
        verify { projectsRepository.deleteProject(projectId) }
    }

    @Test
    fun `getProjectById returns null when project ID does not match any project`() {
        // Given
        val projectId = UUID.randomUUID()
        val differentId = UUID.randomUUID()
        val project = mockk<Project> {
            every { id } returns differentId
        }
        every { projectsRepository.getAllProjects() } returns listOf(project)

        // When
        val result = projectUseCases.getProjectById(projectId)

        // Then
        assertThat(result).isNull()
    }
}