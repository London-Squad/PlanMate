package logic.useCases

import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import logic.repositories.AuthenticationRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class LogoutUseCaseTest {
    private lateinit var logoutUseCase: LogoutUseCase

    private lateinit var authenticationRepository: AuthenticationRepository

    @BeforeEach
    fun setup() {
        authenticationRepository = mockk(relaxed = true)

        logoutUseCase = LogoutUseCase(
            authenticationRepository
        )
    }

    @Test
    fun `logoutUseCase should call logout on authenticationRepository`() {
        runTest {
            logoutUseCase()

            coVerify { authenticationRepository.logout() }
        }
    }
}