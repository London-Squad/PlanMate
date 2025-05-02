package logic.useCases

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import logic.exception.AuthenticationException
import logic.repositories.AuthenticationRepository
import logic.repository.DummyAuthData
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class ChangePasswordUseCaseTest {
    private lateinit var changePasswordUseCase: ChangePasswordUseCase
    private lateinit var authenticationRepository: AuthenticationRepository

    @BeforeEach
    fun setUp() {
        authenticationRepository = mockk(relaxed = true)
        changePasswordUseCase = ChangePasswordUseCase(authenticationRepository)
    }

    @Test
    fun `when we call invoke with valid data should return true`() {
        val user = DummyAuthData.users[2]
        every { authenticationRepository.changePassword(any(), any(), any()) } returns true

        val isChanged = changePasswordUseCase(user.userName, "OldPassword1", "newPassword1")

        assertThat(isChanged).isTrue()
    }

    @ParameterizedTest
    @CsvSource("1", "A1", "as1")
    fun `when we call invoke with invalid username should throw exception`(username: String) {
        assertThrows<AuthenticationException.InvalidUserNameLengthException> {
            changePasswordUseCase(username, "Oldpasswor1", "NewPassword1")
        }
    }

    @ParameterizedTest
    @CsvSource("a", "1", "w1", "asw", "password1")
    fun `when we call invoke with invalid old password validations should throw exception`(oldPassword: String) {
        assertThrows<AuthenticationException.InvalidPasswordException> {
            changePasswordUseCase("testName", oldPassword, "NewPassword1")
        }
    }

    @ParameterizedTest
    @CsvSource("a", "1", "w1", "asw", "password1")
    fun `when we call invoke with invalid new password validations should throw exception`(newPassword: String) {
        assertThrows<AuthenticationException.InvalidPasswordException> {
            changePasswordUseCase("testName", "oldPassword1", newPassword)
        }
    }

    @Test
    fun `when we call invoke with valid data but user not authorized should return false`() {
        every { authenticationRepository.changePassword(any(), any(), any()) } returns false

        val isChanged = changePasswordUseCase("testName", "oldPassword1", "newPassword1")

        assertThat(isChanged).isFalse()
    }
}