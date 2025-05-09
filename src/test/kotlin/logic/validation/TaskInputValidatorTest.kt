package logic.validation

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TaskInputValidatorTest{

  private lateinit var taskInputValidator: TaskInputValidator

  @BeforeEach
  fun setUp() {
   taskInputValidator = TaskInputValidator()
  }

  @Test
  fun `isValidTaskTitle should return false when input is blank`() {
   val input = "   "

   val result = taskInputValidator.isValidTaskTitle(input)

   assertThat(result).isFalse()
  }

  @Test
  fun `isValidTaskTitle should return false when input is longer than 30 character`() {
   val input = "a".repeat(31)

   val result = taskInputValidator.isValidTaskTitle(input)

   assertThat(result).isFalse()
  }

  @Test
  fun `isValidTaskTitle should return true when input is not blank and has 30 character`() {
   val input = "a".repeat(30)

   val result = taskInputValidator.isValidTaskTitle(input)

   assertThat(result).isTrue()
  }

  @Test
  fun `isValidTaskTitle should return true when input is not blank and less 30 character`() {
   val input = "a".repeat(29)

   val result = taskInputValidator.isValidTaskTitle(input)

   assertThat(result).isTrue()
  }

  @Test
  fun `isValidTaskDescription should return true when input is blank`() {
   val input = ""

   val result = taskInputValidator.isValidTaskDescription(input)

   assertThat(result).isTrue()
  }

  @Test
  fun `isValidTaskDescription should return false when input is longer than 150 character`() {
   val input = "a".repeat(151)

   val result = taskInputValidator.isValidTaskDescription(input)

   assertThat(result).isFalse()
  }

  @Test
  fun `isValidTaskDescription should return true when input is not blank and has 150 character`() {
   val input = "a".repeat(150)

   val result = taskInputValidator.isValidTaskDescription(input)

   assertThat(result).isTrue()
  }

  @Test
  fun `isValidTaskDescription should return true when input is not blank and less 150 character`() {
   val input = "a".repeat(149)

   val result = taskInputValidator.isValidTaskDescription(input)

   assertThat(result).isTrue()
  }
 }