package ui.welcomeView

import fakeData
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.entities.User
import logic.exceptions.NoLoggedInUserFoundException
import logic.useCases.GetLoggedInUserUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import ui.loginView.LoginView
import ui.mainMenuView.MainMenuView

class WelcomeViewTest {
    private lateinit var welcomeView: WelcomeView

    private lateinit var cliPrinter: CLIPrinter
    private lateinit var cliReader: CLIReader
    private lateinit var loginView: LoginView
    private lateinit var mainMenuView: MainMenuView
    private lateinit var getLoggedInUserUseCase: GetLoggedInUserUseCase

    @BeforeEach
    fun setup() {
        cliPrinter = mockk(relaxed = true)
        cliReader = mockk(relaxed = true)
        loginView = mockk(relaxed = true)
        mainMenuView = mockk(relaxed = true)
        getLoggedInUserUseCase = mockk(relaxed = true)

        welcomeView = WelcomeView(
            cliPrinter,
            cliReader,
            loginView,
            mainMenuView,
            getLoggedInUserUseCase,
        )
    }

    @Test
    fun `should exit the app when user input is 0`() {
        every { cliReader.getValidInputNumberInRange(any()) } returns 0
        coEvery { getLoggedInUserUseCase.getLoggedInUser() } throws NoLoggedInUserFoundException()

        welcomeView.start()

        verify(exactly = 1) { cliPrinter.cliPrintLn("Exiting the app...") }
    }

    @Test
    fun `should go to login view when user input is 1`() {
        every { cliReader.getValidInputNumberInRange(any()) } answers { 1 } andThenAnswer { 0 }
        coEvery { getLoggedInUserUseCase.getLoggedInUser() } throws NoLoggedInUserFoundException()

        welcomeView.start()

        verify(exactly = 1) { loginView.start() }
    }

    @Test
    fun `should reject user input when user input is not 0 or 1`() {
        every { cliReader.getValidInputNumberInRange(any()) } answers { -1 } andThenAnswer { 0 }
        coEvery { getLoggedInUserUseCase.getLoggedInUser() } throws NoLoggedInUserFoundException()

        welcomeView.start()

        verify(exactly = 2) { cliReader.getValidInputNumberInRange(any()) }
    }

    @ParameterizedTest
    @MethodSource(
        "getFakeUsers"
    )
    fun `should go to main menu directly when there is a logged in user`(user: User) {
        every { cliReader.getValidInputNumberInRange(any()) } returns 0
        coEvery { getLoggedInUserUseCase.getLoggedInUser() } answers { user } andThenThrows NoLoggedInUserFoundException()

        welcomeView.start()

        verify(exactly = 1) { mainMenuView.start(user.type) }
    }

    @ParameterizedTest
    @MethodSource(
        "getFakeUsers"
    )
    fun `should exit if unexpected exception is thrown`(user: User) {
        coEvery { getLoggedInUserUseCase.getLoggedInUser() } throws Exception()

        welcomeView.start()

        verify(exactly = 0) { loginView.start() }
    }

    companion object {
        @JvmStatic
        fun getFakeUsers() = fakeData.users
    }
}
