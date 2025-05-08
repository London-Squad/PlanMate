package ui.loginView

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.entities.User
import logic.useCases.LoginUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.MethodSource
import ui.ViewExceptionHandler
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import ui.mainMenuView.MainMenuView


class LoginViewTest {
    private lateinit var loginView: LoginView
    private lateinit var mainMenuView: MainMenuView
    private lateinit var loginUseCase: LoginUseCase
    private lateinit var cliPrinter: CLIPrinter
    private lateinit var cliReader: CLIReader
    private lateinit var viewExceptionHandler: ViewExceptionHandler


    @BeforeEach
    fun setup() {
        mainMenuView = mockk(relaxed = true)
        loginUseCase = mockk(relaxed = true)
        cliPrinter = mockk(relaxed = true)
        cliReader = mockk(relaxed = true)
        viewExceptionHandler = mockk(relaxed = true)

        loginView = LoginView(
            cliPrinter,
            cliReader,
            loginUseCase,
            mainMenuView,
            viewExceptionHandler
        )
    }

    @ParameterizedTest
    @CsvSource(
        "admin, 123456",
        "user, password",
        "guest, guest123"
    )
    fun `should pass the userName and the password to the useCase`(userName: String, password: String) {
        every { cliReader.getUserInput("username: ") } returns userName
        every { cliReader.getUserInput("password: ") } returns password
        every { viewExceptionHandler.tryCall(any()) } answers {
            firstArg<() -> Unit>().invoke()
            true
        }
        every { loginUseCase(any(), any()) } returns fakeData.mate

        loginView.start()

        verify { loginUseCase(userName, password) }
    }

    @ParameterizedTest
    @MethodSource(
        "getFakeUsers"
    )
    fun `should go to main menu when login success`(user: User) {
        every { loginUseCase(any(), any()) } returns user
        every { viewExceptionHandler.tryCall(any()) } answers {
            firstArg<() -> Unit>().invoke()
            true
        }

        loginView.start()

        verify { mainMenuView.start(user.type) }
    }

    @Test
    fun `should go to welcome when login fail`() {
//        every { loginUseCase(any(), any()) } throws UserNotFoundException()
        every { viewExceptionHandler.tryCall(any()) } answers {
            firstArg<() -> Unit>().invoke()
            false
        }

        loginView.start()

        verify { cliPrinter.cliPrintLn("Login failed, going back to the welcome screen") }
    }


    companion object {
        @JvmStatic
        fun getFakeUsers() = fakeData.users
    }
}