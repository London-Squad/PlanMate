package ui.loginView

//import io.mockk.every
//import io.mockk.mockk
//import io.mockk.verify
//import logic.entities.User
//import logic.useCases.loginUseCase.LoginUseCase
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.Test
//import ui.cliPrintersAndReaders.CLIPrinter
//import ui.cliPrintersAndReaders.CLIReader
//import ui.welcomeView.WelcomeView


//class LoginViewTest {
//    private lateinit var loginView: LoginView
//    private lateinit var welcomeView: WelcomeView
//    private lateinit var loginUseCase: LoginUseCase
//    private lateinit var cliPrinter: CLIPrinter
//    private lateinit var cliReader: CLIReader
//
//    @BeforeEach
//    fun setup() {
//        welcomeView = mockk(relaxed = true)
//        loginUseCase = mockk(relaxed = true)
//        cliPrinter = mockk(relaxed = true)
//        cliReader = mockk(relaxed = true)
//
//        loginView = LoginView(cliPrinter, cliReader, loginUseCase, welcomeView)
//    }
//
//    @Test
//    fun `start should be called at least once`() {
//
//        loginView.startLogin()
//
//        verify(exactly = 1) { loginView.startLogin() }
//    }
//
//    @Test
//    fun `start should print error if username is empty`() {
//        every { cliReader.getUserInput("username: ") } returns ""
//        every { cliReader.getUserInput("password: ") } returns "password"
//
//        loginView.startLogin()
//
//        verify(exactly = 1) { cliPrinter.cliPrintLn("Username is empty. Please try again.") }
//    }
//
//    @Test
//    fun `start should print error if password is empty`() {
//        every { cliReader.getUserInput("username: ") } returns "username"
//        every { cliReader.getUserInput("password: ") } returns ""
//
//        loginView.startLogin()
//
//        verify { cliPrinter.cliPrintLn("Password is empty. Please try again.") }
//    }
//
//    @Test
//    fun `start should print success for valid credentials`() {
//        every { cliReader.getUserInput("username: ") } returns "admin"
//        every { cliReader.getUserInput("password: ") } returns "123456"
//        every { loginUseCase.login("admin", "123456") } returns User(userName = "admin", type = User.Type.ADMIN)
//
//        loginView.startLogin()
//
//        verify { cliPrinter.cliPrintLn("Login successful") }
//    }
//
//    @Test
//    fun `start should print error for invalid credentials`() {
//        every { cliReader.getUserInput("username: ") } returns "admin"
//        every { cliReader.getUserInput("password: ") } returns "123456"
//        every { loginUseCase.login("admin", "123456") } returns null
//
//        loginView.startLogin()
//
//        verify(exactly = 1) { cliPrinter.cliPrintLn("Invalid username or password") }
//    }
//}