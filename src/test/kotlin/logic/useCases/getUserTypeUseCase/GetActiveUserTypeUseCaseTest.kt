package logic.useCases.getUserTypeUseCase

import io.mockk.every
import io.mockk.mockk
import com.google.common.truth.Truth.assertThat
import logic.entities.User
import logic.repositories.AuthenticationRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetActiveUserTypeUseCaseTest {
    private lateinit var getActiveUserTypeUseCase: GetActiveUserTypeUseCase

    private lateinit var authenticationRepository: AuthenticationRepository

    @BeforeEach
    fun setup() {
        authenticationRepository = mockk(relaxed = true)

        getActiveUserTypeUseCase = GetActiveUserTypeUseCase(
            authenticationRepository
        )
    }

    @Test
    fun `getUserType should return null when user is mate`() {
        // Given
        every { authenticationRepository.getActiveUser() } returns null

        // When
        val result = getActiveUserTypeUseCase.getActiveUserType()

        // Then
        assertThat(result).isEqualTo(null)
    }

    @Test
    fun `getUserType should return mate when user type is mate`() {
        // Given
        every { authenticationRepository.getActiveUser() } returns User(userName = "mate", type = User.Type.MATE)

        // When
        val result = getActiveUserTypeUseCase.getActiveUserType()

        // Then
        assertThat(result).isEqualTo(User.Type.MATE)
    }

    @Test
    fun `getUserType should return admin when user type is admin`() {
        // Given
        every { authenticationRepository.getActiveUser() } returns User(userName = "mate", type = User.Type.ADMIN)

        // When
        val result = getActiveUserTypeUseCase.getActiveUserType()

        // Then
        assertThat(result).isEqualTo(User.Type.ADMIN)
    }
}