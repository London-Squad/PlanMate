package logic.useCases

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import logic.entities.User
import logic.repositories.AuthenticationRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

class GetLoggedInUserUseCaseTest {

    private lateinit var getLoggedInUserUseCase: GetLoggedInUserUseCase
    private lateinit var authenticationRepository: AuthenticationRepository

    @BeforeEach
    fun setup() {
        authenticationRepository = mockk(relaxed = true)

        getLoggedInUserUseCase = GetLoggedInUserUseCase(authenticationRepository)
    }

    @ParameterizedTest
    @MethodSource("getFakeUsers")
    fun `getLoggedInUser should return logged in user when user is logged in`(user: User) {
        every { authenticationRepository.getLoggedInUser() } returns user

        val result = getLoggedInUserUseCase.getLoggedInUser()

        assertThat(result).isEqualTo(user)
    }

    companion object {
        @JvmStatic
        fun getFakeUsers() = fakeData.users
    }

}