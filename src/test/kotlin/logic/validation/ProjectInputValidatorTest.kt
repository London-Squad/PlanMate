package logic.validation

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ProjectInputValidatorTest {

    private lateinit var projectInputValidator: ProjectInputValidator

    @BeforeEach
    fun setUp() {
        projectInputValidator = ProjectInputValidator()
    }

    @Test
    fun `isValidProjectTitle should return false when input is blank`() {
        val input = "   "

        val result = projectInputValidator.isValidProjectTitle(input)

        assertThat(result).isFalse()
    }

    @Test
    fun `isValidProjectTitle should return false when input is longer than 30 character`() {
        val input = "a".repeat(31)

        val result = projectInputValidator.isValidProjectTitle(input)

        assertThat(result).isFalse()
    }

    @Test
    fun `isValidProjectTitle should return true when input is not blank and has 30 character`() {
        val input = "a".repeat(30)

        val result = projectInputValidator.isValidProjectTitle(input)

        assertThat(result).isTrue()
    }

    @Test
    fun `isValidProjectTitle should return true when input is not blank and less 30 character`() {
        val input = "a".repeat(29)

        val result = projectInputValidator.isValidProjectTitle(input)

        assertThat(result).isTrue()
    }

    @Test
    fun `isValidProjectDescription should return true when input is blank`() {
        val input = ""

        val result = projectInputValidator.isValidProjectDescription(input)

        assertThat(result).isTrue()
    }

    @Test
    fun `isValidProjectDescription should return false when input is longer than 250 character`() {
        val input = "a".repeat(251)

        val result = projectInputValidator.isValidProjectDescription(input)

        assertThat(result).isFalse()
    }

    @Test
    fun `isValidProjectDescription should return true when input is not blank and has 250 character`() {
        val input = "a".repeat(250)

        val result = projectInputValidator.isValidProjectDescription(input)

        assertThat(result).isTrue()
    }

    @Test
    fun `isValidProjectDescription should return true when input is not blank and less 250 character`() {
        val input = "a".repeat(249)

        val result = projectInputValidator.isValidProjectDescription(input)

        assertThat(result).isTrue()
    }
}