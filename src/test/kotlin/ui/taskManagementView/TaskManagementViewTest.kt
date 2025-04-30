package ui.taskManagementView

import data.catchData.CatchDataMemoryRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.entities.Task
import logic.useCases.ManageTaskUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ui.CLIPrintersAndReaders.CLIPrinter
import ui.CLIPrintersAndReaders.CLIReader
import java.util.*

class TaskManagementViewTest {

    private lateinit var taskManagementView: TaskManagementView
    private lateinit var manageTaskUseCase: ManageTaskUseCase
    private lateinit var catchDataMemoryRepository: CatchDataMemoryRepository
    private lateinit var cliPrinter: CLIPrinter
    private lateinit var cliReader: CLIReader

    @BeforeEach
    fun setup() {
        manageTaskUseCase = mockk(relaxed = true)
        catchDataMemoryRepository = mockk(relaxed = true)
        cliPrinter = mockk(relaxed = true)
        cliReader = mockk(relaxed = true)
        taskManagementView = TaskManagementView(manageTaskUseCase, catchDataMemoryRepository, cliPrinter, cliReader)
    }

    private val task = Task(UUID.randomUUID(), "test", description = "description")

    @Test
    fun `start should tell the user to select task when there is no selected task`() {
        every { catchDataMemoryRepository.getSelectedTask() } returns null

        taskManagementView.start()

        verify(exactly = 1) {
            printLn("please select task first")
        }
    }

    @Test
    fun `start should print task when there is selected task`() {
        every { catchDataMemoryRepository.getSelectedTask() } returns task
        every { cliReader.getUserInput(any()) } returns "0"

        taskManagementView.start()

        verify(exactly = 1) {
            printLn("Task: ${task.title}")
            printLn("Description: ${task.description}")
        }
    }

    @Test
    fun `start should print task management options title option when there is selected task`() {
        every { catchDataMemoryRepository.getSelectedTask() } returns task
        every { cliReader.getUserInput(any()) } returns "0"

        taskManagementView.start()

        verify(exactly = 1) {
            printLn("1. Edit Title")
            printLn("2. Edit description")
            printLn("3. Edit state")
            printLn("4. delete task")
            printLn("0. back")
        }
    }

    @Test
    fun `start should go to projectsView when user input is 1`() {
        every { catchDataMemoryRepository.getSelectedTask() } returns task
        every { cliReader.getUserInput(any()) } answers { "1" } andThenAnswer { "0" }

        taskManagementView.start()

        verify (exactly = 1) {  cliReader.getUserInput("New Title: ") }
    }


    private fun printLn(message: String) {
        cliPrinter.cliPrintLn(message)
    }

}