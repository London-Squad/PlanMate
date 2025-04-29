package ui.mainMenuView

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.entities.User
import logic.repositories.CacheDataRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import ui.loginView.LoginView
import ui.matesManagementView.MatesManagementView
import ui.projectsView.ProjectsView

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MainMenuViewTest {
    private lateinit var mainMenuView: MainMenuView

    private lateinit var cliPrinter: CLIPrinter
    private lateinit var cliReader: CLIReader
    private lateinit var cacheDataRepository: CacheDataRepository
    private lateinit var loginView: LoginView
    private lateinit var projectsView: ProjectsView
    private lateinit var matesManagementView: MatesManagementView

    @BeforeEach
    fun setup() {
        cliPrinter = mockk(relaxed = true)
        cliReader = mockk(relaxed = true)
        cacheDataRepository = mockk(relaxed = true)
        loginView = mockk(relaxed = true)
        projectsView = mockk(relaxed = true)
        matesManagementView = mockk(relaxed = true)

        mainMenuView = MainMenuView(
            cliPrinter,
            cliReader,
            cacheDataRepository,
            loginView,
            projectsView,
            matesManagementView
        )
    }

    @Test
    fun `start should tell the user to login when user didn't login`() {
        // Given
        every { cacheDataRepository.getLoggedInUser() } returns null

        // When
        mainMenuView.start()

        // Then
        verify (exactly = 1) { cliPrinter.printPleaseLoginMessage() }
    }

    @Test
    fun `start should go to login when user didn't login`() {
        // Given
        every { cacheDataRepository.getLoggedInUser() } returns null

        // When
        mainMenuView.start()

        // Then
        verify (exactly = 1) { loginView.start() }
    }

    @Test
    fun `start should print Mates management option when user is admin`() {
        // Given
        every { cacheDataRepository.getLoggedInUser() } returns fakeAdminUser
        every { cliReader.getUserInput(any()) } returns "0"

        // When
        mainMenuView.start()

        // Then
        verify (exactly = 1) { cliPrinter.cliPrintLn("2. Mates management") }
    }

    @Test
    fun `start should not print Mates management option when user is mate`() {
        // Given
        every { cacheDataRepository.getLoggedInUser() } returns fakeMateUser
        every { cliReader.getUserInput(any()) } returns "0"

        // When
        mainMenuView.start()

        // Then
        verify (exactly = 0) { cliPrinter.cliPrintLn("2. Mates management") }
    }

    @ParameterizedTest
    @MethodSource("getUsersList")
    fun `start should go to login when user input is 0`(user: User?) {
        // Given
        every { cacheDataRepository.getLoggedInUser() } returns user
        every { cliReader.getUserInput(any()) } returns "0"

        // When
        mainMenuView.start()

        // Then
        verify (exactly = 1) { loginView.start() }
    }

    @ParameterizedTest
    @MethodSource("getUsersList")
    fun `start should go to projectsView when user input is 1`(user: User?) {
        // Given
        every { cacheDataRepository.getLoggedInUser() } returns user
        every { cliReader.getUserInput(any()) } returns "1"

        // When
        mainMenuView.start()

        // Then
        verify (exactly = 1) { projectsView.start() }
    }

    @Test
    fun `start should go to matesManagementView when user input is 1 and user is admin`() {
        // Given
        every { cacheDataRepository.getLoggedInUser() } returns fakeAdminUser
        every { cliReader.getUserInput(any()) } returns "2"

        // When
        mainMenuView.start()

        // Then
        verify (exactly = 1) { matesManagementView.start() }
    }

    @Test
    fun `start should reject the user input when user input is 2 and user is mate`() {
        // Given
        every { cacheDataRepository.getLoggedInUser() } returns fakeMateUser
        every { cliReader.getUserInput(any()) } answers { "2" } andThenAnswer { "1" }

        // When
        mainMenuView.start()

        // Then
        verify (exactly = 0) { matesManagementView.start() }
    }

    private companion object {
        val fakeAdminUser = User(userName = "fake admin user", type = User.Type.ADMIN)
        val fakeMateUser = User(userName = "fake mate user", type = User.Type.MATE)
        @JvmStatic
        fun getUsersList(): List<User> = listOf(fakeAdminUser, fakeMateUser)
    }

}