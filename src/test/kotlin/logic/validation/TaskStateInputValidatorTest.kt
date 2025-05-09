package logic.validation

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TaskStateInputValidatorTest{

  private lateinit var taskStateInputValidator: TaskStateInputValidator

  @BeforeEach
  fun setUp() {
   taskStateInputValidator = TaskStateInputValidator()
  }

  @Test
  fun `isValidTaskStateTitle should return false when input is blank`() {
   val input = "   "

   val result = taskStateInputValidator.isValidTaskStateTitle(input)

   assertThat(result).isFalse()
  }

  @Test
  fun `isValidTaskTitle should return false when input is longer than 15 character`() {
   val input = "a".repeat(16)

   val result = taskStateInputValidator.isValidTaskStateTitle(input)

   assertThat(result).isFalse()
  }

  @Test
  fun `isValidTaskStateTitle should return true when input is not blank and has 15 character`() {
   val input = "a".repeat(15)

   val result = taskStateInputValidator.isValidTaskStateTitle(input)

   assertThat(result).isTrue()
  }

  @Test
  fun `isValidTaskStateTitle should return true when input is not blank and less 15 character`() {
   val input = "a".repeat(14)

   val result = taskStateInputValidator.isValidTaskStateTitle(input)

   assertThat(result).isTrue()
  }

  @Test
  fun `isValidTaskStateDescription should return true when input is blank`() {
   val input = ""

   val result = taskStateInputValidator.isValidTaskStateDescription(input)

   assertThat(result).isTrue()
  }

  @Test
  fun `isValidTaskStateDescription should return false when input is longer than 100 character`() {
   val input = "a".repeat(101)

   val result = taskStateInputValidator.isValidTaskStateDescription(input)

   assertThat(result).isFalse()
  }

  @Test
  fun `isValidTaskStateDescription should return true when input is not blank and has 100 character`() {
   val input = "a".repeat(100)

   val result = taskStateInputValidator.isValidTaskStateDescription(input)

   assertThat(result).isTrue()
  }

  @Test
  fun `isValidTaskStateDescription should return true when input is not blank and less 100 character`() {
   val input = "a".repeat(99)

   val result = taskStateInputValidator.isValidTaskStateDescription(input)

   assertThat(result).isTrue()
  }
 }