package logic.useCases

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import logic.exceptions.InvalidPasswordException
import logic.exceptions.InvalidUserNameLengthException
import logic.repositories.AuthenticationRepository
import logic.repository.DummyAuthData
import logic.validation.CredentialValidator
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class SignupUseCaseTest {
    private lateinit var signupUseCase: SignupUseCase
    private lateinit var authenticationRepository: AuthenticationRepository
    private lateinit var credentialValidator: CredentialValidator

    @BeforeEach
    fun setUp() {
        authenticationRepository = mockk(relaxed = true)
        credentialValidator = CredentialValidator()
        signupUseCase = SignupUseCase(authenticationRepository, credentialValidator)
    }

    @Test
    fun `when signup with valid data should return true`() {
        val user = DummyAuthData.users[1]
        every { authenticationRepository.register(any(),any()) } returns true

        val isRegistered = signupUseCase(user.userName, "Password12")

        assertThat(isRegistered).isTrue()
    }

    @Test
    fun `when signup with valid data but user not authorized should return false`() {
        val user = DummyAuthData.users[1]
        every { authenticationRepository.register(any(),any()) } returns false

        val isRegistered = signupUseCase(user.userName, "Password12")

        assertThat(isRegistered).isFalse()
    }

    @ParameterizedTest
    @CsvSource("a", "w1", "asw")
    fun `when we call invoke with invalid username validations should throw exception`(username: String) {
        assertThrows<InvalidUserNameLengthException> {
            signupUseCase(username, "testPass1")
        }
    }

    @ParameterizedTest
    @CsvSource("a", "1", "w1", "asw", "password1")
    fun `when we call invoke with invalid password validations should throw exception`(password: String) {
        assertThrows<InvalidPasswordException> {
            signupUseCase("testName", password)
        }
    }
}