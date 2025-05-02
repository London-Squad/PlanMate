package ui.welcomeView

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import ui.loginView.LoginView

class WelcomeViewTest {

    private lateinit var cliPrinter: CLIPrinter
    private lateinit var cliReader: CLIReader
    private lateinit var loginView: LoginView
    private lateinit var welcomeView: WelcomeView

    @BeforeEach
    fun setup() {
        cliPrinter = mockk(relaxed = true)
        cliReader = mockk(relaxed = true)
        loginView = mockk(relaxed = true)

        welcomeView = WelcomeView(cliPrinter, cliReader, loginView)
    }

    @Test
    fun `start should exit the app when user input is 0`() {
        every { cliReader.getValidUserInput(any(), any(), any()) } returns "0"

        welcomeView.start()

        verify(exactly = 1) { cliPrinter.cliPrintLn("Exiting the app...") }
    }

    @Test
    fun `start should go to login view when user input is 1`() {
        every { cliReader.getValidUserInput(any(), any(), any()) } answers { "1" } andThenAnswer { "0" }

        welcomeView.start()

        verify(exactly = 1) { loginView.start() }
    }

    @Test
    fun `start should reject user input when user input is not 0 or 1`() {
        every { cliReader.getValidUserInput(any(), any(), any()) } answers { "-1" } andThenAnswer { "0" }

        welcomeView.start()

        verify(exactly = 2) { cliReader.getValidUserInput(any(), any(), any()) }
    }
}
