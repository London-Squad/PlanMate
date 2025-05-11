//package ui.taskManagementView
//
//import io.mockk.every
//import io.mockk.mockk
//import io.mockk.verify
//import logic.entities.Task
//import logic.useCases.ManageTaskUseCase
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.Test
//import ui.BaseView
//import ui.cliPrintersAndReaders.CLIPrinter
//import ui.cliPrintersAndReaders.CLIReader
//import java.util.*
//
//class TaskDeletionViewTest {
//
//    private lateinit var taskDeletionView: TaskDeletionView
//    private lateinit var cliReader: CLIReader
//    private lateinit var cliPrinter: CLIPrinter
//    private lateinit var manageTaskUseCase: ManageTaskUseCase
//    private lateinit var baseView: BaseView
//
//    @BeforeEach
//    fun setup() {
//
//        cliReader = mockk(relaxed = true)
//        cliPrinter = mockk(relaxed = true)
//        manageTaskUseCase = mockk(relaxed = true)
//        baseView = mockk(relaxed = true)
//
//        taskDeletionView = TaskDeletionView(cliReader, cliPrinter, manageTaskUseCase, baseView)
//    }
//
//    private val task = Task(UUID.randomUUID(), "Fake Task 1", description = "description1")
//
//    @Test
//    fun `deleteTask should cancel deletion when answer is n`() {
//        every { cliReader.getUserConfirmation() } returns false
//
//        taskDeletionView.deleteTask(task)
//
//        verify(exactly = 1) {
//            cliPrinter.cliPrintLn("deletion canceled")
//        }
//    }
//
//    @Test
//    fun `deleteTask should delete task when answer is y`() {
//        every { cliReader.getUserConfirmation() } returns true
//        every { baseView.tryCall(any()) } answers {
//            firstArg<() -> Unit>().invoke()
//            true
//        }
//
//        taskDeletionView.deleteTask(task)
//
//        verify(exactly = 1) {
//            manageTaskUseCase.deleteTask(task.id)
//        }
//    }
//}