package ui.mainMenuView

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.entities.User
import logic.exceptions.NoLoggedInUserIsSavedInCacheException
import logic.useCases.GetLoggedInUserUseCase
import logic.useCases.LogoutUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import ui.matesManagementView.MatesManagementView
import ui.projectsView.ProjectsView

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MainMenuViewTest {
    private lateinit var mainMenuView: MainMenuView

    private lateinit var cliPrinter: CLIPrinter
    private lateinit var cliReader: CLIReader
    private lateinit var getLoggedInUserUseCase: GetLoggedInUserUseCase
    private lateinit var projectsView: ProjectsView
    private lateinit var matesManagementView: MatesManagementView
    private lateinit var logoutUseCase: LogoutUseCase

    @BeforeEach
    fun setup() {
        cliPrinter = mockk(relaxed = true)
        cliReader = mockk(relaxed = true)
        getLoggedInUserUseCase = mockk(relaxed = true)
        logoutUseCase = mockk(relaxed = true)
        projectsView = mockk(relaxed = true)
        matesManagementView = mockk(relaxed = true)

        mainMenuView = MainMenuView(
            cliPrinter,
            cliReader,
            getLoggedInUserUseCase,
            projectsView,
            matesManagementView,
            logoutUseCase
        )
    }

    @Test
    fun `should print Mates management option when user is admin`() {
        // Given
        every { getLoggedInUserUseCase.getLoggedInUser() } returns fakeAdminUser
        every { cliReader.getUserInput(any()) } returns "0"

        // When
        mainMenuView.start()

        // Then
        verify (exactly = 1) { cliPrinter.cliPrintLn("2. Mates management") }
    }

    @Test
    fun `should not print Mates management option when user is mate`() {
        // Given
        every { getLoggedInUserUseCase.getLoggedInUser() } returns fakeMateUser
        every { cliReader.getUserInput(any()) } returns "0"

        // When
        mainMenuView.start()

        // Then
        verify (exactly = 0) { cliPrinter.cliPrintLn("2. Mates management") }
    }

    @ParameterizedTest
    @MethodSource("getUsersList")
    fun `should logout when user input is 0`(user: User) {
        // Given
        every { getLoggedInUserUseCase.getLoggedInUser() } returns user
        every { cliReader.getUserInput(any()) } returns "0"

        // When
        mainMenuView.start()

        // Then
        verify(exactly = 1) { logoutUseCase() }
    }

    @ParameterizedTest
    @MethodSource("getUsersList")
    fun `should go to projectsView when user input is 1`(user: User) {
        // Given
        every { getLoggedInUserUseCase.getLoggedInUser() } returns user
        every { cliReader.getUserInput(any()) } answers { "1" } andThenAnswer { "0" }

        // When
        mainMenuView.start()

        // Then
        verify (exactly = 1) { projectsView.start() }
    }

    @ParameterizedTest
    @MethodSource("getUsersList")
    fun `should reject the user input when user input not 0, 1, or 2`(user: User) {
        // Given
        every { getLoggedInUserUseCase.getLoggedInUser() } returns user
        every { cliReader.getUserInput(any()) } answers { "-1" } andThenAnswer { "0" }

        // When
        mainMenuView.start()

        // Then
        verify(exactly = 2) { cliReader.getUserInput(any()) }
    }

    @Test
    fun `should go to matesManagementView when user input is 2 and user is admin`() {
        // Given
        every { getLoggedInUserUseCase.getLoggedInUser() } returns fakeAdminUser
        every { cliReader.getUserInput(any()) } answers { "2" } andThenAnswer { "0" }

        // When
        mainMenuView.start()

        // Then
        verify (exactly = 1) { matesManagementView.start() }
    }

    @Test
    fun `should reject the user input when user input is 2 and user is mate`() {
        // Given
        every { getLoggedInUserUseCase.getLoggedInUser() } returns fakeMateUser
        every { cliReader.getUserInput(any()) } answers { "2" } andThenAnswer { "0" }

        // When
        mainMenuView.start()

        // Then
        verify (exactly = 0) { matesManagementView.start() }
    }

    @Test
    fun `should end if there is no logged in user`() {
        // Given
        every { getLoggedInUserUseCase.getLoggedInUser() } throws NoLoggedInUserIsSavedInCacheException()
        // When
        mainMenuView.start()

        // Then
        verify(exactly = 1) { cliPrinter.cliPrintLn("please login to continue") }
    }

    private companion object {
        val fakeAdminUser = User(userName = "fakeAdminUser", type = User.Type.ADMIN)
        val fakeMateUser = User(userName = "fakeMateUser", type = User.Type.MATE)
        @JvmStatic
        fun getUsersList(): List<User> = listOf(fakeAdminUser, fakeMateUser)
    }

}