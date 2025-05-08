package ui.mainMenuView

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.entities.User
import logic.useCases.LogoutUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import ui.ViewExceptionHandler
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import ui.matesManagementView.MatesManagementView
import ui.projectsDashboardView.ProjectsDashboardView

class MainMenuViewTest {
    private lateinit var mainMenuView: MainMenuView

    private lateinit var cliPrinter: CLIPrinter
    private lateinit var cliReader: CLIReader
    private lateinit var projectsDashboardView: ProjectsDashboardView
    private lateinit var matesManagementView: MatesManagementView
    private lateinit var logoutUseCase: LogoutUseCase
    private lateinit var viewExceptionHandler: ViewExceptionHandler

    @BeforeEach
    fun setup() {
        cliPrinter = mockk(relaxed = true)
        cliReader = mockk(relaxed = true)
        projectsDashboardView = mockk(relaxed = true)
        matesManagementView = mockk(relaxed = true)
        logoutUseCase = mockk(relaxed = true)
        viewExceptionHandler = mockk(relaxed = true)

        mainMenuView = MainMenuView(
            cliPrinter,
            cliReader,
            projectsDashboardView,
            matesManagementView,
            logoutUseCase,
            viewExceptionHandler
        )
    }

    @Test
    fun `should print Mates management option when user is admin`() {
        mainMenuView.start(User.Type.ADMIN)

        verify(exactly = 1) { cliPrinter.cliPrintLn("2. Mates management") }
    }

    @Test
    fun `should not print Mates management option when user is mate`() {
        mainMenuView.start(User.Type.MATE)

        verify(exactly = 0) { cliPrinter.cliPrintLn("2. Mates management") }
    }

    @ParameterizedTest
    @MethodSource("getFakeUsers")
    fun `should logout when user input is 0`(user: User) {
        every { cliReader.getValidInputNumberInRange(any()) } returns 0
        every { viewExceptionHandler.tryCall(any()) } answers {
            firstArg<() -> Unit>().invoke()
            false
        }

        mainMenuView.start(user.type)

        verify(exactly = 1) { logoutUseCase() }
    }

    @Test
    fun `should accept max input of 1 when user is mate`() {
        every { cliReader.getValidInputNumberInRange(any()) } returns 0

        mainMenuView.start(User.Type.MATE)

        verify { cliReader.getValidInputNumberInRange(1) }
    }

    @Test
    fun `should accept max input of 2 when user is admin`() {
        every { cliReader.getValidInputNumberInRange(any()) } returns 0

        mainMenuView.start(User.Type.ADMIN)

        verify { cliReader.getValidInputNumberInRange(2) }
    }

    @ParameterizedTest
    @MethodSource(
        "getFakeUsers"
    )
    fun `should go to projects when user input is 1`(user: User) {
        every { cliReader.getValidInputNumberInRange(any()) } answers { 1 } andThenAnswer { 0 }

        mainMenuView.start(user.type)

        verify { projectsDashboardView.start(user.type) }
    }

    @Test
    fun `should go to mate management when user input is 2 and user is admin`() {
        every { cliReader.getValidInputNumberInRange(any()) } answers { 2 } andThenAnswer { 0 }

        mainMenuView.start(User.Type.ADMIN)

        verify { matesManagementView.start() }
    }

    companion object {
        @JvmStatic
        fun getFakeUsers() = fakeData.users
    }
}