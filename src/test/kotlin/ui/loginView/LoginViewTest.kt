package ui.loginView

import fakeData
import io.mockk.*
import kotlinx.coroutines.test.runTest
import logic.entities.User
import logic.exceptions.UserNotFoundException
import logic.useCases.LoginUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.MethodSource
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import ui.mainMenuView.MainMenuView


class LoginViewTest {
    private lateinit var loginView: LoginView
    private lateinit var mainMenuView: MainMenuView
    private lateinit var loginUseCase: LoginUseCase
    private lateinit var cliPrinter: CLIPrinter
    private lateinit var cliReader: CLIReader


    @BeforeEach
    fun setup() {
        mainMenuView = mockk(relaxed = true)
        loginUseCase = mockk(relaxed = true)
        cliPrinter = mockk(relaxed = true)
        cliReader = mockk(relaxed = true)

        loginView = LoginView(
            cliPrinter,
            cliReader,
            loginUseCase,
            mainMenuView
        )
    }

    @ParameterizedTest
    @CsvSource(
        "admin, 123456",
        "user, password",
        "guest, guest123"
    )
    fun `should pass the userName and the password to the useCase`(userName: String, password: String) {
        runTest {
            every { cliReader.getValidUserInput(any(), any(), any()) } returnsMany listOf(userName, password)

            loginView.start()

            coVerify { loginUseCase(userName, password) }
        }
    }

    @ParameterizedTest
    @MethodSource(
        "getFakeUsers"
    )
    fun `should go to main menu when login success`(user: User) {
        coEvery { loginUseCase(any(), any()) } returns user

        loginView.start()

        verify { mainMenuView.start(user.type) }
    }

    @Test
    fun `should print 'User could not be found' when login fails`() {
        coEvery { loginUseCase(any(), any()) } throws UserNotFoundException()

        loginView.start()

        verify { cliPrinter.cliPrintLn("User could not be found") }
    }


    companion object {
        @JvmStatic
        fun getFakeUsers() = fakeData.users
    }
}