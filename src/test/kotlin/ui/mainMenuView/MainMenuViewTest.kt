package ui.mainMenuView

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.entities.User
import logic.useCases.getUserTypeUseCase.GetUserTypeUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import ui.CLIPrintersAndReaders.CLIPrinter
import ui.CLIPrintersAndReaders.CLIReader
import ui.loginView.LoginView
import ui.matesManagementView.MatesManagementView
import ui.projectsView.ProjectsView

class MainMenuViewTest {
    private lateinit var mainMenuView: MainMenuView

    private lateinit var cliPrinter: CLIPrinter
    private lateinit var cliReader: CLIReader
    private lateinit var getUserTypeUseCase: GetUserTypeUseCase
    private lateinit var loginView: LoginView
    private lateinit var projectsView: ProjectsView
    private lateinit var matesManagementView: MatesManagementView

    @BeforeEach
    fun setup() {
        cliPrinter = mockk(relaxed = true)
        cliReader = mockk(relaxed = true)
        getUserTypeUseCase = mockk(relaxed = true)
        loginView = mockk(relaxed = true)
        projectsView = mockk(relaxed = true)
        matesManagementView = mockk(relaxed = true)

        mainMenuView = MainMenuView(
            cliPrinter,
            cliReader,
            getUserTypeUseCase,
            loginView,
            projectsView,
            matesManagementView
        )
    }

    @Test
    fun `start should tell the user to login when user didn't login`() {
        // Given
        every { getUserTypeUseCase.getUserType() } returns null
        every { loginView.start() } returns Unit

        // When
        mainMenuView.start()

        // Then
        verify (exactly = 1) { cliPrinter.printPleaseLoginMessage() }
    }

    @Test
    fun `start should go to login when user didn't login`() {
        // Given
        every { getUserTypeUseCase.getUserType() } returns null

        // When
        mainMenuView.start()

        // Then
        verify (exactly = 1) { loginView.start() }
    }

    @Test
    fun `start should print Mates management option when user is admin`() {
        // Given
        every { getUserTypeUseCase.getUserType() } returns User.Type.ADMIN
        every { cliReader.getUserInput(any()) } returns "0"

        // When
        mainMenuView.start()

        // Then
        verify (exactly = 1) { cliPrinter.cliPrintLn("2. Mates management") }
    }

    @Test
    fun `start should not print Mates management option when user is mate`() {
        // Given
        every { getUserTypeUseCase.getUserType() } returns User.Type.MATE
        every { cliReader.getUserInput(any()) } returns "0"

        // When
        mainMenuView.start()

        // Then
        verify (exactly = 0) { cliPrinter.cliPrintLn("2. Mates management") }
    }

    @ParameterizedTest
    @CsvSource("MATE", "ADMIN")
    fun `start should go to login when user input is 0`(userType: User.Type?) {
        // Given
        every { getUserTypeUseCase.getUserType() } returns userType
        every { cliReader.getUserInput(any()) } returns "0"

        // When
        mainMenuView.start()

        // Then
        verify (exactly = 1) { loginView.start() }
    }

    @ParameterizedTest
    @CsvSource("MATE", "ADMIN")
    fun `start should go to projectsView when user input is 1`(userType: User.Type?) {
        // Given
        every { getUserTypeUseCase.getUserType() } returns userType
        every { cliReader.getUserInput(any()) } returns "1"

        // When
        mainMenuView.start()

        // Then
        verify (exactly = 1) { projectsView.start() }
    }

    @Test
    fun `start should go to matesManagementView when user input is 1 and user is admin`() {
        // Given
        every { getUserTypeUseCase.getUserType() } returns User.Type.ADMIN
        every { cliReader.getUserInput(any()) } returns "2"

        // When
        mainMenuView.start()

        // Then
        verify (exactly = 1) { matesManagementView.start() }
    }

    @Test
    fun `start should reject the user input when user input is 2 and user is mate`() {
        // Given
        every { getUserTypeUseCase.getUserType() } returns User.Type.MATE
        every { cliReader.getUserInput(any()) } answers { "2" } andThenAnswer { "1" }

        // When
        mainMenuView.start()

        // Then
        verify (exactly = 0) { matesManagementView.start() }
    }
}