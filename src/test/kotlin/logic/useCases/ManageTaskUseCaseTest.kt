//package logic.useCases
//
//import io.mockk.every
//import io.mockk.mockk
//import io.mockk.mockkStatic
//import io.mockk.verify
//import logic.entities.TaskState
//import logic.repositories.ProjectsRepository
//import logic.repositories.TaskRepository
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.Test
//import ui.taskManagementView.FakeProjectData
//import java.util.UUID
//
//class ManageTaskUseCaseTest {
//
//    private lateinit var manageTaskUseCase: ManageTaskUseCase
//
//    private lateinit var taskRepository: TaskRepository
//    private lateinit var createLogUseCase: CreateLogUseCase
//    private lateinit var projectsRepository: ProjectsRepository
//
//    @BeforeEach
//    fun setup() {
//        taskRepository = mockk(relaxed = true)
//        createLogUseCase = mockk(relaxed = true)
//        projectsRepository = mockk(relaxed = true)
//
//        manageTaskUseCase = ManageTaskUseCase(taskRepository, createLogUseCase, projectsRepository)
//    }
//
//    @Test
//    fun `getTaskByID should call taskRepository getTaskByID`() {
//
//        manageTaskUseCase.getTaskByID(FakeProjectData.tasks[0].id)
//
//        verify(exactly = 1) { taskRepository.getTaskByID(FakeProjectData.tasks[0].id) }
//    }
//
//    @Test
//    fun `addNewTask should call taskRepository addNewTask`() {
//        every { projectsRepository.getProjectById(any()) } returns FakeProjectData.project
//        mockkStatic(UUID::class)
//        every { UUID.randomUUID() } returns FakeProjectData.tasks[0].id
//
//        manageTaskUseCase.addNewTask(FakeProjectData.tasks[0].title, FakeProjectData.tasks[0].description, FakeProjectData.project.id)
//
//        verify(exactly = 1) { taskRepository.addNewTask(FakeProjectData.tasks[0], FakeProjectData.project.id) }
//    }
//
//    @Test
//    fun `editTaskTitle should call taskRepository editTaskTitle`() {
//
//        manageTaskUseCase.editTaskTitle(FakeProjectData.tasks[0].id, "new title")
//
//        verify(exactly = 1) { taskRepository.editTaskTitle(FakeProjectData.tasks[0].id, "new title") }
//    }
//
//    @Test
//    fun `editTaskDescription should call taskRepository editTaskDescription`() {
//
//        manageTaskUseCase.editTaskDescription(FakeProjectData.tasks[0].id, "new description")
//
//        verify(exactly = 1) { taskRepository.editTaskDescription(FakeProjectData.tasks[0].id, "new description") }
//    }
//
//    @Test
//    fun `editTaskState should call taskRepository editTaskState`() {
//        val state = TaskState(UUID.randomUUID(), title = "FakeState", description = "")
//
//        manageTaskUseCase.editTaskState(FakeProjectData.tasks[0].id, state)
//
//        verify(exactly = 1) { taskRepository.editTaskState(FakeProjectData.tasks[0].id, state) }
//    }
//
//
//    @Test
//    fun `deleteTask should call taskRepository deleteTask`() {
//
//        manageTaskUseCase.deleteTask(FakeProjectData.tasks[0].id)
//
//        verify(exactly = 1) { taskRepository.deleteTask(FakeProjectData.tasks[0].id) }
//    }
//}