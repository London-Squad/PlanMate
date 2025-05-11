//package ui.taskManagementView
//
//import io.mockk.every
//import io.mockk.mockk
//import io.mockk.verify
//import logic.useCases.ManageTaskUseCase
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.Test
//import ui.BaseView
//import ui.cliPrintersAndReaders.TaskInputReader
//
//class TaskDescriptionEditionViewTest {
//
//    private lateinit var taskDescriptionEditionView: TaskDescriptionEditionView
//    private lateinit var taskInputReader: TaskInputReader
//    private lateinit var baseView: BaseView
//    private lateinit var manageTaskUseCase: ManageTaskUseCase
//
//    @BeforeEach
//    fun setup() {
//
//        manageTaskUseCase = mockk(relaxed = true)
//        taskInputReader = mockk(relaxed = true)
//        baseView = mockk(relaxed = true)
//
//        taskDescriptionEditionView =
//            TaskDescriptionEditionView(taskInputReader, manageTaskUseCase, baseView)
//    }
//
//    @Test
//    fun `editDescription should ask for new description`() {
//        every { taskInputReader.getValidTaskDescription() } returns "task new description"
//
//        taskDescriptionEditionView.editDescription(FakeProjectData.tasks[0])
//
//        verify(exactly = 1) {
//            taskInputReader.getValidTaskDescription()
//        }
//    }
//
//    @Test
//    fun `editDescription should call manageTaskUseCase editTaskDescription when new description is valid`() {
//        val newDescription = "task new description"
//        every { taskInputReader.getValidTaskDescription() } returns newDescription
//        every { baseView.tryCall(any()) } answers {
//            firstArg<() -> Unit>().invoke()
//            true
//        }
//
//        taskDescriptionEditionView.editDescription(FakeProjectData.tasks[0])
//
//        verify(exactly = 1) {
//            manageTaskUseCase.editTaskDescription(FakeProjectData.tasks[0].id, newDescription)
//        }
//    }
//
//}