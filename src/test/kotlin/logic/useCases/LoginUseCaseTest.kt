package logic.useCases

import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import logic.repositories.AuthenticationRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class LoginUseCaseTest {
    private lateinit var loginUseCase: LoginUseCase

    private lateinit var authenticationRepository: AuthenticationRepository

    @BeforeEach
    fun setup() {
        authenticationRepository = mockk(relaxed = true)

        loginUseCase = LoginUseCase(
            authenticationRepository
        )
    }

    @ParameterizedTest
    @CsvSource(
        "admin, 123456",
        "user, password",
        "guest, guest123"
    )
    fun `logoutUseCase should call logout on authenticationRepository`(userName: String, password: String) {
        runTest {
            loginUseCase(userName, password)

            coVerify { authenticationRepository.login(userName, password) }
        }
    }
}