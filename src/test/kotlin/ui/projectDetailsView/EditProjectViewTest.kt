//package ui.projectDetailsView
//
//import com.google.common.truth.Truth.assertThat
//import io.mockk.*
//import logic.useCases.ManageProjectUseCase
//import ui.ViewExceptionHandler
//import ui.cliPrintersAndReaders.CLIPrinter
//import ui.cliPrintersAndReaders.CLIReader
//import ui.cliPrintersAndReaders.ProjectInputReader
//import ui.taskManagementView.FakeProjectData
//import ui.taskStatesView.TaskStatesView
//import kotlin.test.BeforeTest
//import kotlin.test.Test
//
//class EditProjectViewTest {
//
//    private lateinit var cliPrinter: CLIPrinter
//    private lateinit var cliReader: CLIReader
//    private lateinit var projectInputReader: ProjectInputReader
//    private lateinit var manageProjectUseCase: ManageProjectUseCase
//    private lateinit var taskStatesView: TaskStatesView
//    private lateinit var viewExceptionHandler: ViewExceptionHandler
//    private lateinit var editProjectView: EditProjectView
//
//    @BeforeTest
//    fun setUp() {
//        cliPrinter = mockk(relaxed = true)
//        cliReader = mockk()
//        projectInputReader = mockk()
//        manageProjectUseCase = mockk(relaxed = true)
//        taskStatesView = mockk(relaxed = true)
//        viewExceptionHandler = mockk()
//        editProjectView = EditProjectView(
//            cliPrinter, cliReader, projectInputReader,
//            manageProjectUseCase, taskStatesView, viewExceptionHandler
//        )
//
//        every { viewExceptionHandler.tryCall(any()) } answers {
//            (firstArg() as () -> Unit).invoke(); true
//        }
//    }
//
//    @Test
//    fun `editProject should edit title when user selects 1`() {
//        // given
//        val newTitle = "New Title"
//        every { cliReader.getValidInputNumberInRange(any()) } returns 1
//        every { projectInputReader.getValidProjectTitle() } returns newTitle
//
//        // when
//        editProjectView.editProject(FakeProjectData.project)
//
//        // then
//        verify { manageProjectUseCase.editProjectTitle(FakeProjectData.project.id, newTitle) }
//    }
//
//    @Test
//    fun `editProject should edit description when user selects 2`() {
//        // given
//        val newDesc = "New Desc"
//        every { cliReader.getValidInputNumberInRange(any()) } returns 2
//        every { projectInputReader.getValidProjectDescription() } returns newDesc
//
//        // when
//        editProjectView.editProject(FakeProjectData.project)
//
//        // then
//        verify { manageProjectUseCase.editProjectDescription(FakeProjectData.project.id, newDesc) }
//    }
//
//    @Test
//    fun `editProject should start taskStatesView when user selects 3`() {
//        // given
//        every { cliReader.getValidInputNumberInRange(any()) } returns 3
//
//        // when
//        editProjectView.editProject(FakeProjectData.project)
//
//        // then
//        verify { taskStatesView.start(FakeProjectData.project.id) }
//    }
//
//    @Test
//    fun `editProject should do nothing when user selects 0`() {
//        // given
//        every { cliReader.getValidInputNumberInRange(any()) } returns 0
//
//        // when
//        val result = runCatching { editProjectView.editProject(FakeProjectData.project) }
//
//        // then
//        assertThat(result.isSuccess).isTrue()
//        // No verifications needed since nothing happens
//    }
//}
