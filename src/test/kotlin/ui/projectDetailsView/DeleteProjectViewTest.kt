package ui.projectDetailsView

import io.mockk.*
import logic.useCases.ManageProjectUseCase
import ui.BaseView
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import ui.taskManagementView.FakeProjectData
import kotlin.test.BeforeTest
import kotlin.test.Test

class DeleteProjectViewTest {

    private lateinit var cliPrinter: CLIPrinter
    private lateinit var cliReader: CLIReader
    private lateinit var manageProjectUseCase: ManageProjectUseCase
    private lateinit var baseView: BaseView
    private lateinit var deleteProjectView: DeleteProjectView

    @BeforeTest
    fun setUp() {
        cliPrinter = mockk(relaxed = true)
        cliReader = mockk()
        manageProjectUseCase = mockk(relaxed = true)
        baseView = mockk()
        deleteProjectView = DeleteProjectView(cliPrinter, cliReader, manageProjectUseCase, baseView)
    }

    @Test
    fun `deleteProject should call delete on use case if user confirms`() {
        // given
        every { cliReader.getUserConfirmation() } returns true
        every { baseView.tryCall(any()) } answers {
            (firstArg() as () -> Unit).invoke(); true
        }

        // when
        deleteProjectView.deleteProject(FakeProjectData.project)

        // then
        verify { manageProjectUseCase.deleteProject(FakeProjectData.project.id) }
    }

    @Test
    fun `deleteProject should print success message if deletion succeeds`() {
        // given
        every { cliReader.getUserConfirmation() } returns true
        every { baseView.tryCall(any()) } answers {
            (firstArg() as () -> Unit).invoke(); true
        }

        // when
        deleteProjectView.deleteProject(FakeProjectData.project)

        // then
        verify { cliPrinter.cliPrintLn("Project ${FakeProjectData.project.id} was deleted successfully.") }
    }

    @Test
    fun `deleteProject should print cancellation message if user cancels`() {
        // given
        every { cliReader.getUserConfirmation() } returns false

        // when
        deleteProjectView.deleteProject(FakeProjectData.project)

        // then
        verify { cliPrinter.cliPrintLn("Project deletion canceled") }
    }
}
