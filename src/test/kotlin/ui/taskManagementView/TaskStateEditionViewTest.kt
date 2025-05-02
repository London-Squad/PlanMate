package ui.taskManagementView

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.entities.State
import logic.entities.Task
import logic.useCases.ManageTaskUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import ui.cliPrintersAndReaders.CLIPrinter
import ui.cliPrintersAndReaders.CLIReader
import java.util.*

class TaskStateEditionViewTest {

    private lateinit var taskStateEditionView: TaskStateEditionView
    private lateinit var cliPrinter: CLIPrinter
    private lateinit var cliReader: CLIReader
    private lateinit var manageTaskUseCase: ManageTaskUseCase

    @BeforeEach
    fun setup() {

        manageTaskUseCase = mockk(relaxed = true)
        cliPrinter = mockk(relaxed = true)
        cliReader = mockk(relaxed = true)

        taskStateEditionView = TaskStateEditionView(cliReader, cliPrinter, manageTaskUseCase)

    }

    private val task = Task(UUID.randomUUID(), "Fake Task 1", description = "description1")
    private val statesList = listOf(
        State(UUID.randomUUID(), "Todo", "Todo description"),
        State(UUID.randomUUID(), "Done", "Done description"),
        State(UUID.randomUUID(), "In-progress", "In-progress description"),
    )

    @Test
    fun `editState should return when no state are available`() {

        taskStateEditionView.editState(task, emptyList())

        verify(exactly = 1) {
            printLn("no states available")
        }
    }

    @Test
    fun `editState should print states when available`() {
        every { cliReader.getUserInput(any()) } returns "1"

        taskStateEditionView.editState(task, statesList)

        verify(exactly = 1) {
            statesList.forEachIndexed { index, state ->
                printLn("${index + 1}. ${state.title}")
            }
        }
    }

    @ParameterizedTest
    @CsvSource(
        "1", "2", "3"
    )
    fun `editState should pass the correct state to manageTaskUseCase editTaskState`(input: String) {
        every { cliReader.getUserInput(any()) } returns input
        val state = statesList[input.toInt() - 1]

        taskStateEditionView.editState(task, statesList)

        verify(exactly = 1) {
            manageTaskUseCase.editTaskState(task.id, state)
        }
    }

    @ParameterizedTest
    @CsvSource(
        "4", "n", "hi"
    )
    fun `editState should ask for another input when input is not valid`(input: String) {
        every { cliReader.getUserInput(any()) } answers { input } andThenAnswer { "1" }

        taskStateEditionView.editState(task, statesList)

        verify(exactly = 1) {
            printLn("Invalid input")
        }
    }


    private fun printLn(message: String) {
        cliPrinter.cliPrintLn(message)
    }
}