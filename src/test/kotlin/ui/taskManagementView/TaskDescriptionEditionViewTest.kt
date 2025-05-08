package ui.taskManagementView

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.entities.Task
import logic.useCases.ManageTaskUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ui.ViewExceptionHandler
import ui.cliPrintersAndReaders.TaskInputReader
import java.util.*

class TaskDescriptionEditionViewTest {

    private lateinit var taskDescriptionEditionView: TaskDescriptionEditionView
    private lateinit var taskInputReader: TaskInputReader
    private lateinit var viewExceptionHandler: ViewExceptionHandler
    private lateinit var manageTaskUseCase: ManageTaskUseCase

    @BeforeEach
    fun setup() {

        manageTaskUseCase = mockk(relaxed = true)
        taskInputReader = mockk(relaxed = true)
        viewExceptionHandler = mockk(relaxed = true)
//        cliPrinter = mockk(relaxed = true)

        taskDescriptionEditionView =
            TaskDescriptionEditionView(taskInputReader, manageTaskUseCase, viewExceptionHandler)

    }

    private val task = Task(UUID.randomUUID(), "Fake Task 1", description = "description1")
    private val newDescription = "task new description"

    @Test
    fun `editDescription should ask for new description`() {
        every { taskInputReader.getValidTaskDescription() } returns newDescription

        taskDescriptionEditionView.editDescription(task)

        verify(exactly = 1) {
            taskInputReader.getValidTaskDescription()
        }
    }

    @Test
    fun `editDescription should call manageTaskUseCase editTaskDescription when new description is valid`() {
        every { taskInputReader.getValidTaskDescription() } returns newDescription
        every { viewExceptionHandler.tryCall(any()) } answers {
            firstArg<() -> Unit>().invoke()
            true
        }

        taskDescriptionEditionView.editDescription(task)

        verify(exactly = 1) {
            manageTaskUseCase.editTaskDescription(task.id, newDescription)
        }
    }

    @Test
    fun `description can be blank`() {
        every { taskInputReader.getValidTaskDescription() } returns ""
        every { viewExceptionHandler.tryCall(any()) } answers {
            firstArg<() -> Unit>().invoke()
            true
        }

        taskDescriptionEditionView.editDescription(task)

        verify(exactly = 1) {
            manageTaskUseCase.editTaskDescription(task.id, "")
        }
    }
}