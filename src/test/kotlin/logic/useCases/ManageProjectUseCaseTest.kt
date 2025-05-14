package logic.useCases

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import logic.entities.Project
import logic.repositories.ProjectsRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals

class ManageProjectUseCaseTest {

    private lateinit var projectsRepository: ProjectsRepository
    private lateinit var createLogUseCase: CreateLogUseCase
    private lateinit var manageProjectUseCase: ManageProjectUseCase

    @BeforeEach
    fun setUp() {
        projectsRepository = mockk(relaxed = true)
        createLogUseCase = mockk(relaxed = true)
        manageProjectUseCase = ManageProjectUseCase(projectsRepository, createLogUseCase)
    }

    @Test
    fun `getAllProjects should return all projects`() = runTest {
        val projects = listOf(fakeProject)
        coEvery { projectsRepository.getAllProjects() } returns projects

        val result = manageProjectUseCase.getAllProjects()

        assertEquals(projects, result)
        coVerify { projectsRepository.getAllProjects() }
    }

    @Test
    fun `getProjectById should return the correct project`() = runTest {
        val projectId = UUID.randomUUID()
        val project = fakeProject.copy(id = projectId)
        coEvery { projectsRepository.getProjectById(projectId) } returns project

        val result = manageProjectUseCase.getProjectById(projectId)

        assertEquals(project, result)
        coVerify { projectsRepository.getProjectById(projectId) }
    }

    @Test
    fun `editProjectTitle should update the title and log the change`() = runTest {
        val projectId = UUID.randomUUID()
        val oldTitle = "Old Title"
        val newTitle = "New Title"
        val project = fakeProject.copy(id = projectId, title = oldTitle)
        coEvery { projectsRepository.getProjectById(projectId) } returns project

        manageProjectUseCase.editProjectTitle(projectId, newTitle)

        coVerify {
            projectsRepository.editProjectTitle(projectId, newTitle)
            createLogUseCase.logEntityTitleEdition(projectId, oldTitle, newTitle)
        }
    }

    @Test
    fun `editProjectDescription should update the description and log the change`() = runTest {
        val projectId = UUID.randomUUID()
        val oldDescription = "Old Description"
        val newDescription = "New Description"
        val project = fakeProject.copy(id = projectId, description = oldDescription)
        coEvery { projectsRepository.getProjectById(projectId) } returns project

        manageProjectUseCase.editProjectDescription(projectId, newDescription)

        coVerify {
            projectsRepository.editProjectDescription(projectId, newDescription)
            createLogUseCase.logEntityDescriptionEdition(projectId, oldDescription, newDescription)
        }
    }

    @Test
    fun `deleteProject should delete the project and log the deletion`() = runTest {
        val projectId = UUID.randomUUID()

        manageProjectUseCase.deleteProject(projectId)

        coVerify {
            projectsRepository.deleteProject(projectId)
            createLogUseCase.logEntityDeletion(projectId)
        }
    }

    private companion object {
        val fakeProject = Project(
            id = UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
            title = "fake Project",
            description = "This is a fake project for testing purposes",
        )
    }
}