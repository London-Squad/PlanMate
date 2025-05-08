package logic.useCases

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import logic.entities.Task
import logic.entities.TaskState
import logic.repositories.ProjectsRepository
import logic.repositories.TaskRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID

class ManageTaskUseCaseTest {

    private lateinit var taskRepository: TaskRepository
    private lateinit var manageTaskUseCase: ManageTaskUseCase
    private lateinit var createLogUseCase: CreateLogUseCase
    private lateinit var projectsRepository: ProjectsRepository

    @BeforeEach
    fun setup() {
        taskRepository = mockk(relaxed = true)
        createLogUseCase = mockk(relaxed = true)
        projectsRepository = mockk(relaxed = true)
        manageTaskUseCase = ManageTaskUseCase(taskRepository, createLogUseCase, projectsRepository)
    }

    private val task = Task(UUID.randomUUID(), "test", description = "description")

    @Test
    fun `addNewTask should call taskRepository addNewTask`() {
        val projectId = UUID.randomUUID()
        every { projectsRepository.getProjectById(any()).tasksStates.first() } returns TaskState.NoTaskState
        mockkStatic(UUID::class)
        every { UUID.randomUUID() } returns task.id

        manageTaskUseCase.addNewTask(task.title, task.description, projectId)

        verify(exactly = 1) { taskRepository.addNewTask(task, projectId) }
    }

    @Test
    fun `editTaskTitle should call taskRepository editTaskTitle`() {

        manageTaskUseCase.editTaskTitle(task.id, "new title")

        verify(exactly = 1) { taskRepository.editTaskTitle(task.id, "new title") }
    }

    @Test
    fun `editTaskDescription should call taskRepository editTaskDescription`() {

        manageTaskUseCase.editTaskDescription(task.id, "new description")

        verify(exactly = 1) { taskRepository.editTaskDescription(task.id, "new description") }
    }

    @Test
    fun `editTaskState should call taskRepository editTaskState`() {
        val state = TaskState(UUID.randomUUID(), title = "FakeState", description = "")

        manageTaskUseCase.editTaskState(task.id, state)

        verify(exactly = 1) { taskRepository.editTaskState(task.id, state) }
    }


    @Test
    fun `deleteTask should call taskRepository deleteTask`() {

        manageTaskUseCase.deleteTask(task.id)

        verify(exactly = 1) { taskRepository.deleteTask(task.id) }
    }
}