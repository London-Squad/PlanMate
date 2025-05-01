package ui.taskManagementView

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.entities.Task
import logic.useCases.ManageTaskUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import java.util.*

class TaskDeletionViewTest {

    private lateinit var taskDeletionView: TaskDeletionView
    private lateinit var cliPrinter: CLIPrinter
    private lateinit var cliReader: CLIReader
    private lateinit var manageTaskUseCase: ManageTaskUseCase

    @BeforeEach
    fun setup() {

        manageTaskUseCase = mockk(relaxed = true)
        cliPrinter = mockk(relaxed = true)
        cliReader = mockk(relaxed = true)

        taskDeletionView = TaskDeletionView(cliReader, cliPrinter, manageTaskUseCase)
    }

    private val task = Task(UUID.randomUUID(), "Fake Task 1", description = "description1")

    @Test
    fun `deleteTask should ask for delete confirmation`() {
        every { cliReader.getUserInput(any()) } returns "n"

        taskDeletionView.deleteTask(task)

        verify(exactly = 1) {
            cliReader.getUserInput("Are you sure to delete the task? (y/n): ")
        }
    }

    @Test
    fun `deleteTask should cancel deletion when answer is n`() {
        every { cliReader.getUserInput(any()) } returns "n"

        taskDeletionView.deleteTask(task)

        verify(exactly = 1) {
            cliPrinter.cliPrintLn("deletion canceled")        }
    }

    @Test
    fun `deleteTask should delete task when answer is y`() {
        every { cliReader.getUserInput(any()) } returns "y"

        taskDeletionView.deleteTask(task)

        verify(exactly = 1) {
            manageTaskUseCase.deleteTask(task.id)
            cliPrinter.cliPrintLn("task ${task.title} was deleted")
        }
    }

    @ParameterizedTest
    @CsvSource(
        "5", "srg", "6576", " ny,  n jhk "
    )
    fun `deleteTask should print invalid input when user input is not an option`(input: String) {
        every { cliReader.getUserInput(any()) } answers { input } andThenAnswer { "n" }

        taskDeletionView.deleteTask(task)

        verify(exactly = 1) {
            cliPrinter.cliPrintLn("Invalid input")
        }
    }
}