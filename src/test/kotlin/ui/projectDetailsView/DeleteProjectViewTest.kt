package ui.projectDetailsView

import io.mockk.*
import logic.entities.Project
import logic.useCases.ManageProjectUseCase
import ui.ViewExceptionHandler
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import java.util.*
import kotlin.test.BeforeTest
import kotlin.test.Test

class DeleteProjectViewTest {

    private lateinit var cliPrinter: CLIPrinter
    private lateinit var cliReader: CLIReader
    private lateinit var manageProjectUseCase: ManageProjectUseCase
    private lateinit var viewExceptionHandler: ViewExceptionHandler
    private lateinit var deleteProjectView: DeleteProjectView

    private val project = Project(
        id = UUID.randomUUID(),
        title = "Test",
        description = "Test Description",
        tasks = emptyList(),
        tasksStates = emptyList()
    )

    @BeforeTest
    fun setUp() {
        cliPrinter = mockk(relaxed = true)
        cliReader = mockk()
        manageProjectUseCase = mockk(relaxed = true)
        viewExceptionHandler = mockk()
        deleteProjectView = DeleteProjectView(cliPrinter, cliReader, manageProjectUseCase, viewExceptionHandler)
    }

    @Test
    fun `deleteProject should call delete on use case if user confirms`() {
        // given
        every { cliReader.getUserConfirmation() } returns true
        every { viewExceptionHandler.tryCall(any()) } answers {
            (firstArg() as () -> Unit).invoke(); true
        }

        // when
        deleteProjectView.deleteProject(project)

        // then
        verify { manageProjectUseCase.deleteProject(project.id) }
    }

    @Test
    fun `deleteProject should print success message if deletion succeeds`() {
        // given
        every { cliReader.getUserConfirmation() } returns true
        every { viewExceptionHandler.tryCall(any()) } answers {
            (firstArg() as () -> Unit).invoke(); true
        }

        // when
        deleteProjectView.deleteProject(project)

        // then
        verify { cliPrinter.cliPrintLn("Project ${project.id} was deleted successfully.") }
    }

    @Test
    fun `deleteProject should print cancellation message if user cancels`() {
        // given
        every { cliReader.getUserConfirmation() } returns false

        // when
        deleteProjectView.deleteProject(project)

        // then
        verify { cliPrinter.cliPrintLn("Project deletion canceled") }
    }
}
