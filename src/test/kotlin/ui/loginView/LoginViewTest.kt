package ui.loginView

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.usecases.loginUseCase.LoginUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader


class LoginViewTest {
    private lateinit var loginView: LoginView
    private lateinit var loginUseCase: LoginUseCase
    private lateinit var cliPrinter: CLIPrinter
    private lateinit var cliReader: CLIReader

    @BeforeEach
    fun setup() {
        loginUseCase = mockk(relaxed = true)
        cliPrinter = mockk(relaxed = true)
        cliReader = mockk(relaxed = true)

        loginView = LoginView(cliPrinter, cliReader, loginUseCase)
    }

    @Test
    fun `start should be called at least once`() {

        loginView.start()

        verify(exactly = 1) { loginView.start() }
    }

    @Test
    fun `start should print error if username is empty`() {
        every { cliReader.getUserInput("username: ") } returns ""
        every { cliReader.getUserInput("password: ") } returns "password"

        loginView.start()

        verify(exactly = 1) { cliPrinter.cliPrintLn("Username is empty. Please try again.") }
    }

    @Test
    fun `start should print error if password is empty`() {
        every { cliReader.getUserInput("username: ") } returns "username"
        every { cliReader.getUserInput("password: ") } returns ""

        loginView.start()

        verify { cliPrinter.cliPrintLn("Password is empty. Please try again.") }
    }
}