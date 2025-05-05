package logic.useCases

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import logic.repositories.AuthenticationRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class LogoutUseCaseTest {
    private lateinit var logoutUseCase: LogoutUseCase
    private lateinit var authenticationRepository: AuthenticationRepository

    @BeforeEach
    fun setUp() {
        authenticationRepository = mockk(relaxed = true)
        logoutUseCase = LogoutUseCase(authenticationRepository)
    }

    @Test
    fun `when we call invoke to logout should return true`() {
        every { authenticationRepository.logout() } returns true

        val isLoggedOut = logoutUseCase()

        assertThat(isLoggedOut).isTrue()
    }

    @Test
    fun `when we call invoke to logout but user not authorized should return false`() {
        every { authenticationRepository.logout() } returns false

        val isLoggedOut = logoutUseCase()

        assertThat(isLoggedOut).isFalse()
    }
}