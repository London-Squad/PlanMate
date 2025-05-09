package logic.useCases

import com.google.common.truth.Truth.assertThat
import io.mockk.*
import logic.entities.Project
import logic.repositories.ProjectsRepository
import java.util.*
import kotlin.test.BeforeTest
import kotlin.test.Test

class ManageProjectUseCaseTest {

    private lateinit var projectsRepository: ProjectsRepository
    private lateinit var createLogUseCase: CreateLogUseCase
    private lateinit var manageProjectUseCase: ManageProjectUseCase

    private val projectId = UUID.randomUUID()
    private val project = Project(
        id = projectId,
        title = "Old Title",
        description = "Old Description",
        tasks = emptyList(),
        tasksStates = emptyList()
    )

    @BeforeTest
    fun setUp() {
        projectsRepository = mockk()
        createLogUseCase = mockk(relaxed = true)
        manageProjectUseCase = ManageProjectUseCase(projectsRepository, createLogUseCase)
    }

    @Test
    fun `getAllProjects should return all projects`() {
        // given
        val expectedProjects = listOf(project)
        every { projectsRepository.getAllProjects() } returns expectedProjects

        // when
        val result = manageProjectUseCase.getAllProjects()

        // then
        assertThat(result).isEqualTo(expectedProjects)
    }

    @Test
    fun `getProjectById should return the correct project`() {
        // given
        every { projectsRepository.getProjectById(projectId) } returns project

        // when
        val result = manageProjectUseCase.getProjectById(projectId)

        // then
        assertThat(result).isEqualTo(project)
    }

    @Test
    fun `editProjectTitle should update title and log the change`() {
        // given
        every { projectsRepository.getProjectById(projectId) } returns project
        every { projectsRepository.editProjectTitle(projectId, "New Title") } just Runs

        // when
        manageProjectUseCase.editProjectTitle(projectId, "New Title")

        // then
        verify { createLogUseCase.logEntityTitleEdition(project, "Old Title", "New Title") }
    }

    @Test
    fun `editProjectDescription should update description and log the change`() {
        // given
        every { projectsRepository.getProjectById(projectId) } returns project
        every { projectsRepository.editProjectDescription(projectId, "New Desc") } just Runs

        // when
        manageProjectUseCase.editProjectDescription(projectId, "New Desc")

        // then
        verify { createLogUseCase.logEntityDescriptionEdition(project, "Old Description", "New Desc") }
    }

    @Test
    fun `deleteProject should delete the project and log the deletion`() {
        // given
        every { projectsRepository.getProjectById(projectId) } returns project
        every { projectsRepository.deleteProject(projectId) } just Runs

        // when
        manageProjectUseCase.deleteProject(projectId)

        // then
        verify { createLogUseCase.logEntityDeletion(project) }
    }
}
