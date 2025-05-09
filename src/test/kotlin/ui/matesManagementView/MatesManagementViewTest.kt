//package ui.matesManagementView
//
//import io.mockk.*
//import logic.entities.User
//import logic.useCases.GetLoggedInUserUseCase
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.Test
//import ui.cliPrintersAndReaders.CLIPrinter
//import ui.cliPrintersAndReaders.CLIReader
//
//class MatesManagementViewTest {
//
//    private lateinit var cliReader: CLIReader
//    private lateinit var cliPrinter: CLIPrinter
//    private lateinit var getLoggedInUserUseCase: GetLoggedInUserUseCase
//    private lateinit var mateCreationView: MateCreationView
//    private lateinit var view: MatesManagementView
//
//    private val adminUser = User(userName = "admin", type = User.Type.ADMIN)
//    private val mateUser = User(userName = "mate", type = User.Type.MATE)
//
//    @BeforeEach
//    fun setUp() {
//        cliReader = mockk()
//        cliPrinter = mockk(relaxed = true)
//        getLoggedInUserUseCase = mockk()
//        mateCreationView = mockk(relaxed = true)
//        view = MatesManagementView(cliPrinter, cliReader, getLoggedInUserUseCase, mateCreationView)
//    }
//
//    @Test
//    fun `start should not allow non-admin user`() {
//        every { getLoggedInUserUseCase.getLoggedInUser() } returns mateUser
//
//        view.start()
//
//        verify { cliPrinter.cliPrintLn("Error: Only admins can manage mates.") }
//        verify(exactly = 0) { cliReader.getUserInput(any()) }
//    }
//
//
//    @Test
//    fun `start should call createMate when user selects 1`() {
//        every { getLoggedInUserUseCase.getLoggedInUser() } returns adminUser
//        every { cliReader.getUserInput(any()) } returnsMany listOf("1", "0")
//
//        view.start()
//
//        verifySequence {
//            cliPrinter.cliPrintLn("Mates Management Menu")
//            cliPrinter.cliPrintLn("1. Create New Mate")
//            cliPrinter.cliPrintLn("0. Back")
//            cliReader.getUserInput("Your option: ")
//            mateCreationView.createMate()
//            cliPrinter.cliPrintLn("Mates Management Menu")
//            cliPrinter.cliPrintLn("1. Create New Mate")
//            cliPrinter.cliPrintLn("0. Back")
//            cliReader.getUserInput("Your option: ")
//        }
//    }
//
//    @Test
//    fun `start should exit on selecting 0`() {
//        every { getLoggedInUserUseCase.getLoggedInUser() } returns adminUser
//        every { cliReader.getUserInput(any()) } returns "0"
//
//        view.start()
//
//        verifySequence {
//            cliPrinter.cliPrintLn("Mates Management Menu")
//            cliPrinter.cliPrintLn("1. Create New Mate")
//            cliPrinter.cliPrintLn("0. Back")
//            cliReader.getUserInput("Your option: ")
//        }
//        verify(exactly = 0) { mateCreationView.createMate() }
//    }
//
//    @Test
//    fun `start should handle invalid option and re-prompt`() {
//        every { getLoggedInUserUseCase.getLoggedInUser() } returns adminUser
//        every { cliReader.getUserInput(any()) } returnsMany listOf("5", "abc", "1", "0")
//
//        view.start()
//
//        verify(atLeast = 2) { cliPrinter.cliPrintLn("Invalid option") }
//        verify(exactly = 1) { mateCreationView.createMate() }
//    }
//}
