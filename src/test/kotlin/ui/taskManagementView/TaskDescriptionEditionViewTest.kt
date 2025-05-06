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
//class TaskDescriptionEditionViewTest {
//
//    private lateinit var taskDescriptionEditionView: TaskDescriptionEditionView
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
//        taskDescriptionEditionView = TaskDescriptionEditionView(cliReader, cliPrinter, manageTaskUseCase)
//
//    }
//
//    private val task = Task(UUID.randomUUID(), "Fake Task 1", description = "description1")
//
//    @Test
//    fun `editDescription should ask for new description`() {
//        every { cliReader.getUserInput(any()) } returns "task new description"
//
//        taskDescriptionEditionView.editDescription(task)
//
//        verify(exactly = 1) {
//            cliReader.getUserInput("New Description (150 character or less): ")
//        }
//    }
//
//    @Test
//    fun `editDescription should call manageTaskUseCase editTaskDescription when new description is valid`() {
//        val newDescription = "task new description"
//        every { cliReader.getUserInput(any()) } returns newDescription
//
//        taskDescriptionEditionView.editDescription(task)
//
//        verify(exactly = 1) {
//            manageTaskUseCase.editTaskDescription(task.id, newDescription)
//        }
//    }
//
//    @Test
//    fun `description should not exceed 30 character`() {
//        val longDescriptionExceed30Char = "a".repeat(151)
//        val validDescriptionWith30Char = "a".repeat(150)
//        every { cliReader.getUserInput(any()) } answers { longDescriptionExceed30Char } andThenAnswer { validDescriptionWith30Char }
//
//        taskDescriptionEditionView.editDescription(task)
//
//        verify(exactly = 1) {
//            cliPrinter.cliPrintLn("Invalid description")
//        }
//    }
//
//    @Test
//    fun `description can be blank`() {
//        every { cliReader.getUserInput(any()) } answers { "    " }
//
//        taskDescriptionEditionView.editDescription(task)
//
//        verify(exactly = 1) {
//            manageTaskUseCase.editTaskDescription(task.id, "")
//        }
//    }
//
//}