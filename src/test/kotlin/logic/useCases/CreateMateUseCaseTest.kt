package logic.useCases

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import logic.exceptions.UnauthorizedOperationException
import logic.repositories.AuthenticationRepository
import logic.repositories.UserRepository
import logic.validation.CredentialValidator
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertFailsWith

class CreateMateUseCaseTest {

    private lateinit var authenticationRepository: AuthenticationRepository
    private lateinit var userRepository: UserRepository
    private lateinit var credentialValidator: CredentialValidator
    private lateinit var createMateUseCase: CreateMateUseCase

    @BeforeEach
    fun setUp() {
        authenticationRepository = mockk(relaxed = true)
        userRepository = mockk(relaxed = true)
        credentialValidator = mockk(relaxed = true)
        createMateUseCase = CreateMateUseCase(
            authenticationRepository,
            userRepository,
            credentialValidator
        )
    }

    @Test
    fun `createMate should successfully create mate when called by admin`() = runTest {
        val username = "newMate"
        val password = "securePassword123"
        coEvery { authenticationRepository.getLoggedInUser() } returns fakeData.admin
        coEvery { credentialValidator.validateUserName(username) } returns Unit
        coEvery { credentialValidator.validatePassword(password) } returns Unit
        coEvery { userRepository.addMate(username, password) } returns Unit

        createMateUseCase.createMate(username, password)

        coVerify {
            authenticationRepository.getLoggedInUser()
            credentialValidator.validateUserName(username)
            credentialValidator.validatePassword(password)
            userRepository.addMate(username, password)
        }
    }

    @Test
    fun `createMate should throw UnauthorizedOperationException when called by non-admin`() = runTest {
        val username = "newMate"
        val password = "securePassword123"
        coEvery { authenticationRepository.getLoggedInUser() } returns fakeData.mate

        assertFailsWith<UnauthorizedOperationException> {
            createMateUseCase.createMate(username, password)
        }

        coVerify {
            authenticationRepository.getLoggedInUser()
        }
        coVerify(exactly = 0) {
            credentialValidator.validateUserName(username)
            credentialValidator.validatePassword(password)
            userRepository.addMate(username, password)
        }
    }

    @Test
    fun `createMate should throw exception when username validation fails`() = runTest {
        val username = "invalid@username"
        val password = "securePassword123"
        coEvery { authenticationRepository.getLoggedInUser() } returns fakeData.admin
        coEvery { credentialValidator.validateUserName(username) } throws IllegalArgumentException("Invalid username")

        assertFailsWith<IllegalArgumentException> {
            createMateUseCase.createMate(username, password)
        }

        coVerify {
            authenticationRepository.getLoggedInUser()
            credentialValidator.validateUserName(username)
        }
        coVerify(exactly = 0) {
            credentialValidator.validatePassword(password)
            userRepository.addMate(username, password)
        }
    }

    @Test
    fun `createMate should throw exception when password validation fails`() = runTest {
        val username = "newMate"
        val password = "weak"
        coEvery { authenticationRepository.getLoggedInUser() } returns fakeData.admin
        coEvery { credentialValidator.validateUserName(username) } returns Unit
        coEvery { credentialValidator.validatePassword(password) } throws IllegalArgumentException("Password too weak")

        assertFailsWith<IllegalArgumentException> {
            createMateUseCase.createMate(username, password)
        }

        coVerify {
            authenticationRepository.getLoggedInUser()
            credentialValidator.validateUserName(username)
            credentialValidator.validatePassword(password)
        }
        coVerify(exactly = 0) {
            userRepository.addMate(username, password)
        }
    }
}