package logic.useCases

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import logic.repositories.AuthenticationRepository
import logic.repository.DummyAuthData
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class LoginUseCaseTest {
    private lateinit var loginUseCase: LoginUseCase
    private lateinit var authenticationRepository: AuthenticationRepository

    @BeforeEach
    fun setUp() {
        authenticationRepository = mockk(relaxed = true)
        loginUseCase = LoginUseCase(authenticationRepository)
    }

    @Test
    fun `when we call invoke with valid user credentials should return user`() {
        val user = DummyAuthData.users[0]
        every { authenticationRepository.login(user.userName, any()) } returns user

        val loggedInUser = loginUseCase(user.userName, "Password1")

        assertThat(loggedInUser).isEqualTo(user)
    }
}