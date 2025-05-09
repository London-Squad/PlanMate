package ui.projectDetailsView

import com.google.common.truth.Truth.assertThat
import io.mockk.*
import logic.entities.Project
import logic.useCases.ManageProjectUseCase
import ui.ViewExceptionHandler
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import ui.cliPrintersAndReaders.ProjectInputReader
import ui.taskStatesView.TaskStatesView
import java.util.*
import kotlin.test.BeforeTest
import kotlin.test.Test

class EditProjectViewTest {

    private lateinit var cliPrinter: CLIPrinter
    private lateinit var cliReader: CLIReader
    private lateinit var projectInputReader: ProjectInputReader
    private lateinit var manageProjectUseCase: ManageProjectUseCase
    private lateinit var taskStatesView: TaskStatesView
    private lateinit var viewExceptionHandler: ViewExceptionHandler
    private lateinit var editProjectView: EditProjectView

    private val project = Project(
        id = UUID.randomUUID(),
        title = "Old Title",
        description = "Old Description",
        tasks = emptyList(),
        tasksStates = emptyList()
    )

    @BeforeTest
    fun setUp() {
        cliPrinter = mockk(relaxed = true)
        cliReader = mockk()
        projectInputReader = mockk()
        manageProjectUseCase = mockk(relaxed = true)
        taskStatesView = mockk(relaxed = true)
        viewExceptionHandler = mockk()
        editProjectView = EditProjectView(
            cliPrinter, cliReader, projectInputReader,
            manageProjectUseCase, taskStatesView, viewExceptionHandler
        )
    }

    @Test
    fun `editProject should edit title when user selects 1`() {
        // given
        every { cliReader.getValidInputNumberInRange(any()) } returns 1
        every { projectInputReader.getValidProjectTitle() } returns "New Title"
        every { viewExceptionHandler.tryCall(any()) } answers {
            (firstArg() as () -> Unit).invoke(); true
        }

        // when
        editProjectView.editProject(project)

        // then
        verify { manageProjectUseCase.editProjectTitle(project.id, "New Title") }
    }

    @Test
    fun `editProject should edit description when user selects 2`() {
        // given
        every { cliReader.getValidInputNumberInRange(any()) } returns 2
        every { projectInputReader.getValidProjectDescription() } returns "New Desc"
        every { viewExceptionHandler.tryCall(any()) } answers {
            (firstArg() as () -> Unit).invoke(); true
        }

        // when
        editProjectView.editProject(project)

        // then
        verify { manageProjectUseCase.editProjectDescription(project.id, "New Desc") }
    }

    @Test
    fun `editProject should start taskStatesView when user selects 3`() {
        // given
        every { cliReader.getValidInputNumberInRange(any()) } returns 3

        // when
        editProjectView.editProject(project)

        // then
        verify { taskStatesView.start(project.id) }
    }

    @Test
    fun `editProject should do nothing when user selects 0`() {
        // given
        every { cliReader.getValidInputNumberInRange(any()) } returns 0

        // when
        val result = runCatching { editProjectView.editProject(project) }

        // then
        assertThat(result.isSuccess).isTrue()
        // No verifications needed since nothing happens
    }
}
