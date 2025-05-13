//package logic.useCases
//
//import com.google.common.truth.Truth.assertThat
//import io.mockk.*
//import logic.repositories.ProjectsRepository
//import ui.taskManagementView.FakeProjectData
//import kotlin.test.BeforeTest
//import kotlin.test.Test
//
//class ManageProjectUseCaseTest {
//
//    private lateinit var projectsRepository: ProjectsRepository
//    private lateinit var createLogUseCase: CreateLogUseCase
//    private lateinit var manageProjectUseCase: ManageProjectUseCase
//
//
//    @BeforeTest
//    fun setUp() {
//        projectsRepository = mockk(relaxed = true)
//        createLogUseCase = mockk(relaxed = true)
//        manageProjectUseCase = ManageProjectUseCase(projectsRepository, createLogUseCase)
//    }
//
//    @Test
//    fun `getAllProjects should return all projects`() {
//        // given
//        val expectedProjects = listOf(FakeProjectData.project)
//        every { projectsRepository.getAllProjects() } returns expectedProjects
//
//        // when
//        val result = manageProjectUseCase.getAllProjects()
//
//        // then
//        assertThat(result).isEqualTo(expectedProjects)
//    }
//
//    @Test
//    fun `getProjectById should return the correct project`() {
//        // given
//        every { projectsRepository.getProjectById(any()) } returns FakeProjectData.project
//
//        // when
//        val result = manageProjectUseCase.getProjectById(FakeProjectData.project.id)
//
//        // then
//        assertThat(result).isEqualTo(FakeProjectData.project)
//    }
//
//    @Test
//    fun `editProjectTitle should call projectsRepository editProjectTitle`() {
//        // given
//        val newTitle = "New Title"
//
//        // when
//        manageProjectUseCase.editProjectTitle(FakeProjectData.project.id, newTitle)
//
//        // then
//        verify { projectsRepository.editProjectTitle(FakeProjectData.project.id, newTitle) }
//    }
//
//    @Test
//    fun `editProjectTitle should update title and log the change`() {
//        // given
//        val newTitle = "New Title"
//        every { projectsRepository.getProjectById(any()) } returns FakeProjectData.project
//        every { projectsRepository.editProjectTitle(any(), any()) } just Runs
//
//        // when
//        manageProjectUseCase.editProjectTitle(FakeProjectData.project.id, newTitle)
//
//        // then
//        verify {
//            createLogUseCase.logEntityTitleEdition(
//                FakeProjectData.project,
//                FakeProjectData.project.title,
//                newTitle
//            )
//        }
//    }
//
//    @Test
//    fun `editProjectDescription should call projectsRepository editProjectDescription`() {
//        // given
//        val newDesc = "New Desc"
//
//        // when
//        manageProjectUseCase.editProjectDescription(FakeProjectData.project.id, newDesc)
//
//        // then
//        verify { projectsRepository.editProjectDescription(FakeProjectData.project.id, newDesc) }
//    }
//
//    @Test
//    fun `editProjectDescription should update description and log the change`() {
//        // given
//        val newDesc = "New Desc"
//        every { projectsRepository.getProjectById(FakeProjectData.project.id) } returns FakeProjectData.project
//        every { projectsRepository.editProjectDescription(FakeProjectData.project.id, newDesc) } just Runs
//
//        // when
//        manageProjectUseCase.editProjectDescription(FakeProjectData.project.id, newDesc)
//
//        // then
//        verify {
//            createLogUseCase.logEntityDescriptionEdition(
//                FakeProjectData.project,
//                FakeProjectData.project.description,
//                newDesc
//            )
//        }
//    }
//
//    @Test
//    fun `deleteProject should call projectsRepository deleteProject`() {
//        // when
//        manageProjectUseCase.deleteProject(FakeProjectData.project.id)
//
//        // then
//        verify { projectsRepository.deleteProject(FakeProjectData.project.id) }
//    }
//
//    @Test
//    fun `deleteProject should delete the project and log the deletion`() {
//        // given
//        every { projectsRepository.getProjectById(FakeProjectData.project.id) } returns FakeProjectData.project
//        every { projectsRepository.deleteProject(FakeProjectData.project.id) } just Runs
//
//        // when
//        manageProjectUseCase.deleteProject(FakeProjectData.project.id)
//
//        // then
//        verify { createLogUseCase.logEntityDeletion(FakeProjectData.project) }
//    }
//}
