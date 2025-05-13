//package ui.matesManagementView
//
//import io.mockk.every
//import io.mockk.mockk
//import io.mockk.verify
//import logic.entities.User
//import logic.exceptions.*
//import logic.useCases.CreateMateUseCase
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.Test
//import ui.cliPrintersAndReaders.CLIPrinter
//import ui.cliPrintersAndReaders.CLIReader
//
//class MateCreationViewTest {
//
//    private lateinit var createMateUseCase: CreateMateUseCase
//    private lateinit var cliPrinter: CLIPrinter
//    private lateinit var cliReader: CLIReader
//    private lateinit var view: MateCreationView
//
//    private val newUser = User(userName = "testuser", type = User.Type.MATE)
//
//    @BeforeEach
//    fun setUp() {
//        createMateUseCase = mockk()
//        cliPrinter = mockk(relaxed = true)
//        cliReader = mockk()
//        view = MateCreationView(createMateUseCase, cliPrinter, cliReader)
//    }
//
//    @Test
//    fun `should create mate successfully`() {
//        every { cliReader.getUserInput("Enter username: ") } returns "testuser"
//        every { cliReader.getUserInput("Enter password: ") } returns "Password123"
//        every {
//            createMateUseCase.createMate(
//                "testuser",
//                "Password123"
//            )
//        } returns cliPrinter.cliPrintLn("Mate created successfully: ${newUser.userName}")
//
//        view.createMate()
//
//        verify {
//            cliReader.getUserInput("Enter username: ")
//            cliReader.getUserInput("Enter password: ")
//            createMateUseCase.createMate("testuser", "Password123")
//            cliPrinter.cliPrintLn("Mate created successfully: testuser")
//        }
//    }
//
//    @Test
//    fun `should show error if user not logged in`() {
//        every { cliReader.getUserInput("Enter username: ") } returns "testuser"
//        every { cliReader.getUserInput("Enter password: ") } returns "Password123"
//        every {
//            createMateUseCase.createMate(
//                "testuser",
//                "Password123"
//            )
//        } throws UserNotFoundException()
//
//        view.createMate()
//
//        verify {
//            cliPrinter.cliPrintLn("Error: No user is logged in.")
//        }
//    }
//
//    @Test
//    fun `should show error if unauthorized access by non-admin`() {
//        every { cliReader.getUserInput("Enter username: ") } returns "testuser"
//        every { cliReader.getUserInput("Enter password: ") } returns "Password123"
//        every {
//            createMateUseCase.createMate("testuser", "Password123")
//        } throws UnauthorizedAccessException()
//
//        view.createMate()
//
//        verify {
//            cliPrinter.cliPrintLn("Error: Only admins can access mate management.")
//        }
//    }
//
//    @Test
//    fun `should show error if username is too short`() {
//        every { cliReader.getUserInput("Enter username: ") } returns "abc"
//        every { cliReader.getUserInput("Enter password: ") } returns "Pass123"
//        every {
//            createMateUseCase.createMate("abc", "Pass123")
//        } throws InvalidUserNameLengthException()
//
//        view.createMate()
//
//        verify {
//            cliPrinter.cliPrintLn("Error: Username should be at least 4 characters, alphanumeric, no whitespace.")
//        }
//    }
//
//    @Test
//    fun `should show error if password is invalid`() {
//        every { cliReader.getUserInput("Enter username: ") } returns "testuser"
//        every { cliReader.getUserInput("Enter password: ") } returns "short"
//        every {
//            createMateUseCase.createMate("testuser", "short")
//        } throws InvalidPasswordException()
//
//        view.createMate()
//
//        verify {
//            cliPrinter.cliPrintLn("Error: Password should be 6 to 12 character and includes at least 1 lower case and 1 uppercase character.")
//        }
//    }
//
//    @Test
//    fun `should show error if username is already taken`() {
//        every { cliReader.getUserInput("Enter username: ") } returns "testuser"
//        every { cliReader.getUserInput("Enter password: ") } returns "Password123"
//        every {
//            createMateUseCase.createMate("testuser", "Password123")
//        } throws UserNameAlreadyExistsException()
//
//        view.createMate()
//
//        verify {
//            cliPrinter.cliPrintLn("Error: Username is already taken.")
//        }
//    }
//
//    @Test
//    fun `should show error if registration fails`() {
//        every { cliReader.getUserInput("Enter username: ") } returns "testuser"
//        every { cliReader.getUserInput("Enter password: ") } returns "Password123"
//        every {
//            createMateUseCase.createMate("testuser", "Password123")
//        } throws RegistrationFailedException()
//
//        view.createMate()
//
//        verify {
//            cliPrinter.cliPrintLn("Error: Failed to save user data, Try Later !")
//        }
//    }
//
//    @Test
//    fun `should show error for unexpected exception`() {
//        every { cliReader.getUserInput("Enter username: ") } returns "testuser"
//        every { cliReader.getUserInput("Enter password: ") } returns "Password123"
//        every {
//            createMateUseCase.createMate("testuser", "Password123")
//        } throws RuntimeException("Unexpected error")
//
//        view.createMate()
//
//        verify {
//            cliPrinter.cliPrintLn("Unexpected error: Unexpected error")
//        }
//    }
//}
