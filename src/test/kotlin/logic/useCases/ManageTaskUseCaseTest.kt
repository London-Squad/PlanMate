package logic.useCases

import io.mockk.mockk
import io.mockk.verify
import logic.entities.State
import logic.entities.Task
import logic.repositories.TaskRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID

class ManageTaskUseCaseTest {

    private lateinit var taskRepository: TaskRepository
    private lateinit var manageTaskUsecase: ManageTaskUseCase

    @BeforeEach
    fun setup() {
        taskRepository = mockk(relaxed = true)
        manageTaskUsecase = ManageTaskUseCase(taskRepository)
    }

    private val task = Task(UUID.randomUUID(), "test", description = "description")

    @Test
    fun `editTaskTitle should call taskRepository editTaskTitle`() {

        manageTaskUsecase.editTaskTitle(task.id, "new title")

        verify(exactly = 1) { taskRepository.editTaskTitle(task.id, "new title") }
    }

    @Test
    fun `editTaskDescription should call taskRepository editTaskDescription`() {

        manageTaskUsecase.editTaskDescription(task.id, "new description")

        verify(exactly = 1) { taskRepository.editTaskDescription(task.id, "new description") }
    }

    @Test
    fun `editTaskState should call taskRepository editTaskState`() {
        val state = State(UUID.randomUUID(), title = "FakeState", description = "")

        manageTaskUsecase.editTaskState(task.id, state)

        verify(exactly = 1) { taskRepository.editTaskState(task.id, state) }
    }


    @Test
    fun `deleteTask should call taskRepository deleteTask`() {

        manageTaskUsecase.deleteTask(task.id)

        verify(exactly = 1) { taskRepository.deleteTask(task.id) }
    }


}