package ui.welcomeView

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.exceptions.NoLoggedInUserIsSavedInCacheException
import logic.useCases.GetLoggedInUserUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import ui.loginView.LoginView
import ui.mainMenuView.MainMenuView

class WelcomeViewTest {

    private lateinit var cliPrinter: CLIPrinter
    private lateinit var cliReader: CLIReader
    private lateinit var loginView: LoginView
    private lateinit var welcomeView: WelcomeView
    private lateinit var mainMenuView: MainMenuView
    private lateinit var getLoggedInUserUseCase: GetLoggedInUserUseCase

    @BeforeEach
    fun setup() {
        cliPrinter = mockk(relaxed = true)
        cliReader = mockk(relaxed = true)
        loginView = mockk(relaxed = true)
        mainMenuView = mockk(relaxed = true)
        getLoggedInUserUseCase = mockk(relaxed = true)

        welcomeView = WelcomeView(cliPrinter, cliReader, loginView, mainMenuView, getLoggedInUserUseCase)
    }

    @Test
    fun `should exit the app when user input is 0`() {
        every { cliReader.getValidUserInput(any(), any(), any()) } returns "0"
        every { getLoggedInUserUseCase.getLoggedInUser() } throws NoLoggedInUserIsSavedInCacheException()

        welcomeView.start()

        verify(exactly = 1) { cliPrinter.cliPrintLn("Exiting the app...") }
    }

    @Test
    fun `should go to login view when user input is 1`() {
        every { cliReader.getValidUserInput(any(), any(), any()) } answers { "1" } andThenAnswer { "0" }
        every { getLoggedInUserUseCase.getLoggedInUser() } throws NoLoggedInUserIsSavedInCacheException()

        welcomeView.start()

        verify(exactly = 1) { loginView.start() }
    }

    @Test
    fun `should reject user input when user input is not 0 or 1`() {
        every { cliReader.getValidUserInput(any(), any(), any()) } answers { "-1" } andThenAnswer { "0" }
        every { getLoggedInUserUseCase.getLoggedInUser() } throws NoLoggedInUserIsSavedInCacheException()

        welcomeView.start()

        verify(exactly = 2) { cliReader.getValidUserInput(any(), any(), any()) }
    }
}
