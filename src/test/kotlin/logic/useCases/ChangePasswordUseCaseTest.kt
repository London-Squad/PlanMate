package logic.useCases

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import logic.repositories.MateRepository
import logic.repository.DummyAuthData
import logic.validation.CredentialValidator
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ChangePasswordUseCaseTest {
    private lateinit var changePasswordUseCase: ChangePasswordUseCase
    private lateinit var mateRepository: MateRepository
    private lateinit var credentialValidator: CredentialValidator

    @BeforeEach
    fun setUp() {
        mateRepository = mockk(relaxed = true)
        credentialValidator = CredentialValidator()
        changePasswordUseCase = ChangePasswordUseCase(mateRepository)
    }

    @Test
    fun `when we call invoke with valid data should return true`() {
        val user = DummyAuthData.users[2]
        every { mateRepository.changeMatePassword(any(), any(), any()) } returns true

        val isChanged = changePasswordUseCase(user.userName, "OldPassword1", "newPassword1")

        assertThat(isChanged).isTrue()
    }

    @Test
    fun `when we call invoke with valid data but user not authorized should return false`() {
        every { mateRepository.changeMatePassword(any(), any(), any()) } returns false

        val isChanged = changePasswordUseCase("testName", "oldPassword1", "newPassword1")

        assertThat(isChanged).isFalse()
    }
}