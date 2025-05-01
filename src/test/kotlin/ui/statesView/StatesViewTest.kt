package ui.statesView

import io.mockk.*
import logic.entities.State
import logic.useCases.ManageStateUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ui.CLIPrintersAndReaders.CLIPrinter
import ui.CLIPrintersAndReaders.CLIReader
import java.util.*
import kotlin.test.assertTrue

class StatesViewTest {

    private lateinit var view: StatesView
    private lateinit var useCase: ManageStateUseCase
    private lateinit var printer: CLIPrinter
    private lateinit var reader: CLIReader
    private val projectId = UUID.randomUUID()

    @BeforeEach
    fun setUp() {
        useCase = mockk(relaxed = true)
        printer = mockk(relaxed = true)
        reader = mockk(relaxed = true)
        view = StatesView(printer, reader, useCase)
    }

    @Test
    fun `start should Print No States Message  when States List Is Empty`() {
        every { useCase.getStates(projectId) } returns emptyList()
        every { reader.getUserInput(any()) } returnsMany listOf("1", "0")

        view.start(projectId)

        verify {
            printer.cliPrintLn("No states available.")
        }
    }

    @Test
    fun `addState  should Capture Correct Title And Description when Input Is Valid`() {
        val stateSlot = slot<State>()
        val title = "ToDo"
        val description = "Initial state"

        every { reader.getUserInput(any()) } returnsMany listOf("2", title, description, "0")
        every { useCase.addState(capture(stateSlot), projectId) } just Runs

        view.start(projectId)

        assertTrue(stateSlot.isCaptured)
        assertTrue(stateSlot.captured.title == title)
        assertTrue(stateSlot.captured.description == description)
    }

    @Test
    fun `deleteState should Call UseCase when User Confirms With Valid UUID`() {
        val state = State(title = "Done", description = "desc")
        val stateId = state.id

        every { useCase.getStates(projectId) } returns listOf(state)
        every { reader.getUserInput(any()) } returnsMany listOf("4", stateId.toString(), "y", "0")

        view.start(projectId)

        verify { useCase.deleteState(stateId) }
    }

    @Test
    fun `deleteState should Cancel And Print Message when User Declines Confirmation`() {
        val state = State(title = "InProgress", description = "test")
        every { useCase.getStates(projectId) } returns listOf(state)

        every { reader.getUserInput(any()) } returnsMany listOf("4", state.id.toString(), "n", "0")

        view.start(projectId)

        verify(exactly = 0) { useCase.deleteState(state.id) }
        verify { printer.cliPrintLn("Deletion canceled.") }
    }

    @Test
    fun `editState  should Call Edit Title And Description when Inputs Are Valid`() {
        val state = State(title = "Old", description = "desc")
        val newTitle = "NewTitle"
        val newDesc = "NewDesc"

        every { useCase.getStates(projectId) } returns listOf(state)
        every { reader.getUserInput(any()) } returnsMany listOf("3", state.id.toString(), "1", newTitle, "3", state.id.toString(), "2", newDesc, "0")

        view.start(projectId)

        verify { useCase.editStateTitle(state.id, newTitle) }
        verify { useCase.editStateDescription(state.id, newDesc) }
    }
    @Test
    fun `start should Print Invalid Option Message when User Chooses Unknown Menu Option`() {
        every { reader.getUserInput(any()) } returnsMany listOf("6", "0")

        view.start(projectId)

        verify { printer.cliPrintLn("Invalid option.") }
    }
    @Test
    fun `editState should Retry when User Enters Invalid Then Valid UUID`() {
        val state = State(title = "ToEdit", description = "desc")
        val validId = state.id

        every { useCase.getStates(projectId) } returns listOf(state)
        every { reader.getUserInput(any()) } returnsMany listOf("3", "invalid-uuid", validId.toString(), "1", "NewTitle", "0")

        view.start(projectId)

        verify { printer.cliPrintLn("Invalid UUID format.") }
        verify { useCase.editStateTitle(validId, "NewTitle") }
    }
    @Test
    fun `deleteState should Retry when User Enters Invalid Then Valid UUID`() {
        val state = State(title = "ToDelete", description = "desc")
        val validId = state.id

        every { useCase.getStates(projectId) } returns listOf(state)
        every { reader.getUserInput(any()) } returnsMany listOf("4", "invalid-uuid", validId.toString(), "y", "0")

        view.start(projectId)

        verify { printer.cliPrintLn("Invalid UUID format.") }
        verify { useCase.deleteState(validId) }
    }

}