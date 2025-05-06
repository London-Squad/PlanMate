//package ui.taskManagementView
//
//import io.mockk.every
//import io.mockk.mockk
//import io.mockk.verify
//import logic.entities.Task
//import logic.useCases.ManageTaskUseCase
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.Test
//import ui.cliPrintersAndReaders.CLIPrinter
//import ui.cliPrintersAndReaders.CLIReader
//import java.util.*
//
//class TaskTitleEditionViewTest {
//
//    private lateinit var taskTitleEditionView: TaskTitleEditionView
//    private lateinit var cliPrinter: CLIPrinter
//    private lateinit var cliReader: CLIReader
//    private lateinit var manageTaskUseCase: ManageTaskUseCase
//
//    @BeforeEach
//    fun setup() {
//
//        manageTaskUseCase = mockk(relaxed = true)
//        cliPrinter = mockk(relaxed = true)
//        cliReader = mockk(relaxed = true)
//
//        taskTitleEditionView = TaskTitleEditionView(cliReader, cliPrinter, manageTaskUseCase)
//
//    }
//
//    private val task = Task(UUID.randomUUID(), "Fake Task 1", description = "description1")
//
//    @Test
//    fun `editTaskTitle should ask for new title`() {
//        every { cliReader.getUserInput(any()) } returns "task new title"
//
//        taskTitleEditionView.editTitle(task)
//
//        verify(exactly = 1) {
//            cliReader.getUserInput("New Title (30 character or less): ")
//        }
//    }
//
//    @Test
//    fun `editTaskTitle should call manageTaskUseCase editTaskTitle when new title is valid`() {
//        val newTitle = "task new title"
//        every { cliReader.getUserInput(any()) } returns newTitle
//
//        taskTitleEditionView.editTitle(task)
//
//        verify(exactly = 1) {
//            manageTaskUseCase.editTaskTitle(task.id, newTitle)
//        }
//    }
//
//    @Test
//    fun `new title should not exceed 30 character`() {
//        val longTitleExceed30Char = "a".repeat(31)
//        val validTitleWith30Char = "a".repeat(30)
//        every { cliReader.getUserInput(any()) } answers { longTitleExceed30Char } andThenAnswer { validTitleWith30Char }
//
//        taskTitleEditionView.editTitle(task)
//
//        verify(exactly = 1) {
//            cliPrinter.cliPrintLn("Invalid Title")
//        }
//    }
//
//    @Test
//    fun `title should not be blank`() {
//        every { cliReader.getUserInput(any()) } answers { "    " } andThenAnswer { "not blank title" }
//
//        taskTitleEditionView.editTitle(task)
//
//        verify(exactly = 1) {
//            cliPrinter.cliPrintLn("Invalid Title")
//        }
//    }
////    private fun createDummyString(repeat : Int, alpha : Char) = alpha.toString().repeat(repeat)
//
//
//}