//package ui.taskManagementView
//
//import io.mockk.every
//import io.mockk.mockk
//import io.mockk.verify
//import logic.entities.Task
//import logic.useCases.ManageTaskUseCase
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.Test
//import ui.ViewExceptionHandler
//import ui.cliPrintersAndReaders.TaskInputReader
//import java.util.*
//
//class TaskTitleEditionViewTest {
//
//    private lateinit var taskTitleEditionView: TaskTitleEditionView
//    private lateinit var taskInputReader: TaskInputReader
//    private lateinit var viewExceptionHandler: ViewExceptionHandler
//    private lateinit var manageTaskUseCase: ManageTaskUseCase
//
//    @BeforeEach
//    fun setup() {
//
//        manageTaskUseCase = mockk(relaxed = true)
//        taskInputReader = mockk(relaxed = true)
//        viewExceptionHandler = mockk(relaxed = true)
//
//        taskTitleEditionView = TaskTitleEditionView(taskInputReader, manageTaskUseCase, viewExceptionHandler)
//
//    }
//
//    private val newTitle = "task new title"
//    private val task = Task(UUID.randomUUID(), "Fake Task 1", description = "description1")
//
//    @Test
//    fun `editTaskTitle should ask for new title`() {
//        taskTitleEditionView.editTitle(task)
//
//        verify(exactly = 1) {
//            taskInputReader.getValidTaskTitle()
//        }
//    }
//
//    @Test
//    fun `editTaskTitle should call manageTaskUseCase editTaskTitle when new title is valid`() {
//        every { taskInputReader.getValidTaskTitle() } returns newTitle
//        every { viewExceptionHandler.tryCall(any()) } answers {
//            firstArg<() -> Unit>().invoke()
//            true
//        }
//
//        taskTitleEditionView.editTitle(task)
//
//        verify(exactly = 1) {
//            manageTaskUseCase.editTaskTitle(task.id, newTitle)
//        }
//    }
//}