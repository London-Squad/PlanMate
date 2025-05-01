package logic.useCases

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import logic.exception.AuthenticationException
import logic.repositories.AuthenticationRepository
import logic.repositories.CacheDataRepository
import logic.repository.DummyAuthData
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.AfterTest

class LoginUseCaseTest {
    private lateinit var loginUseCase: LoginUseCase
    private lateinit var authenticationRepository: AuthenticationRepository
    private lateinit var cacheDataRepository: CacheDataRepository

    @BeforeEach
    fun setUp() {
        authenticationRepository = mockk(relaxed = true)
        cacheDataRepository = mockk(relaxed = true)
        loginUseCase = LoginUseCase(authenticationRepository, cacheDataRepository)
    }

    @ParameterizedTest
    @CsvSource("a", "w1", "asw")
    fun `when we call invoke with invalid username validations should throw exception`(username: String) {
        assertThrows<AuthenticationException.InvalidUserNameLengthException> {
            loginUseCase.invoke(username, "testPass1")
        }
    }

    @ParameterizedTest
    @CsvSource("a", "1", "w1", "asw", "password1")
    fun `when we call invoke with invalid password validations should throw exception`(password: String) {
        assertThrows<AuthenticationException.InvalidPasswordException> {
            loginUseCase("testName", password)
        }
    }

    @Test
    fun `when we call invoke with valid user credentials should return user`() {
        val user = DummyAuthData.users[0]
        every { authenticationRepository.login(user.userName, any()) } returns user

        val loggedInUser = loginUseCase(user.userName, "Password1")

        assertThat(loggedInUser).isEqualTo(user)
    }

    @AfterTest
    fun tearDown() {
    }
}