package logic.validation

import logic.exceptions.InvalidPasswordException
import logic.exceptions.InvalidUserNameLengthException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class CredentialValidatorTest {
    private lateinit var credentialValidator: CredentialValidator

    @BeforeEach
    fun setup() {
        credentialValidator = CredentialValidator()
    }

    @ParameterizedTest
    @CsvSource("1", "A1", "as1")
    fun `when we call takeIfValidNameOrThrowException with invalid username should throw exception`(username: String) {
        assertThrows<InvalidUserNameLengthException> {
            credentialValidator.takeIfValidNameOrThrowException(username)
        }
    }

    @ParameterizedTest
    @CsvSource("a", "1", "w1", "asw", "password1")
    fun `when we call invoke with invalid password validations should throw exception`(password: String) {
        assertThrows<InvalidPasswordException> {
            credentialValidator.takeIfValidPasswordOrThrowException(password)
        }
    }
}