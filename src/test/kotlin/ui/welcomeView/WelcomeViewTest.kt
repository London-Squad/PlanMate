package ui.welcomeView

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.entities.User
import logic.repositories.CacheDataRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import ui.cliPrintersAndReaders.CLIPrinter
import ui.loginView.LoginView
import ui.mainMenuView.MainMenuView

class WelcomeViewTest {

    private lateinit var cliPrinter: CLIPrinter
    private lateinit var loginView: LoginView
    private lateinit var mainMenuView: MainMenuView
    private lateinit var cacheDataRepository: CacheDataRepository
    private lateinit var welcomeView: WelcomeView

    @BeforeEach
    fun setup() {
        cliPrinter = mockk(relaxed = true)
        cacheDataRepository = mockk(relaxed = true)
        loginView = mockk(relaxed = true)
        mainMenuView = mockk(relaxed = true)

        welcomeView = WelcomeView(cliPrinter, loginView, mainMenuView, cacheDataRepository)
    }


    @Test
    fun `start should tell the user to login when no user is logged in`() {
        every { cacheDataRepository.getLoggedInUser() } returns null

        welcomeView.start()

        verify (exactly = 1) { cliPrinter.printPleaseLoginMessage() }
    }

    @Test
    fun `start should go to login when no user is logged in`() {
        every { cacheDataRepository.getLoggedInUser() } returns null

        welcomeView.start()

        verify (exactly = 1) { loginView.start() }
    }

    @ParameterizedTest
    @MethodSource("getUsersList")
    fun `start should print welcome user message when user is logged in`(user: User) {
        every { cacheDataRepository.getLoggedInUser() } returns user

        welcomeView.start()

        verify (exactly = 1) { cliPrinter.cliPrintLn("welcome ${user.userName} to PlanMate V1.0") }
    }

    @ParameterizedTest
    @MethodSource("getUsersList")
    fun `start should go to main menu when user is logged in`(user: User) {
        every { cacheDataRepository.getLoggedInUser() } returns user

        welcomeView.start()

        verify (exactly = 1) { mainMenuView.start() }
    }

    private companion object {
        val fakeAdminUser = User(userName = "fakeAdminUser", type = User.Type.ADMIN)
        val fakeMateUser = User(userName = "fakeMateUser", type = User.Type.MATE)
        @JvmStatic
        fun getUsersList(): List<User> = listOf(fakeAdminUser, fakeMateUser)
    }
}
