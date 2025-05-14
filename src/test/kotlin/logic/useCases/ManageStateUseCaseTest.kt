package logic.useCases

import io.mockk.*
import kotlinx.coroutines.test.runTest
import logic.entities.TaskState
import logic.repositories.TaskStatesRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals


class ManageStateUseCaseTest {

    private lateinit var taskStatesRepository: TaskStatesRepository
    private lateinit var createLogUseCase: CreateLogUseCase
    private lateinit var manageStateUseCase: ManageStateUseCase

    @BeforeEach
    fun setUp() {
        taskStatesRepository = mockk(relaxed = true)
        createLogUseCase = mockk(relaxed = true)
        manageStateUseCase = ManageStateUseCase(taskStatesRepository, createLogUseCase)
    }

    @Test
    fun `addState should add a new state and log the creation`() = runTest {
        val projectId = UUID.randomUUID()
        val title = "New State"
        val description = "State Description"
        val capturedTaskState = slot<TaskState>()
        coJustRun { taskStatesRepository.addNewTaskState(capture(capturedTaskState), projectId) }

        manageStateUseCase.addState(title, description, projectId)

        coVerify { taskStatesRepository.addNewTaskState(any(), projectId) }
        assertEquals(title, capturedTaskState.captured.title)
        assertEquals(description, capturedTaskState.captured.description)
        assertEquals(projectId, capturedTaskState.captured.projectId)
        coVerify { createLogUseCase.logEntityCreation(capturedTaskState.captured.id) }
    }

    @Test
    fun `editStateTitle should update the title and log the change`() = runTest {
        val stateId = fakeTaskState.id
        val newTitle = "Updated Title"
        coEvery { taskStatesRepository.getTaskStateById(stateId) } returns fakeTaskState

        manageStateUseCase.editStateTitle(stateId, newTitle)

        coVerify { taskStatesRepository.editTaskStateTitle(stateId, newTitle) }
        coVerify { createLogUseCase.logEntityTitleEdition(stateId, fakeTaskState.title, newTitle) }
    }

    @Test
    fun `editStateDescription should update the description and log the change`() = runTest {
        val stateId = fakeTaskState.id
        val newDescription = "Updated Description"
        coEvery { taskStatesRepository.getTaskStateById(stateId) } returns fakeTaskState

        manageStateUseCase.editStateDescription(stateId, newDescription)

        coVerify { taskStatesRepository.editTaskStateDescription(stateId, newDescription) }
        coVerify { createLogUseCase.logEntityDescriptionEdition(stateId, fakeTaskState.description, newDescription) }
    }

    @Test
    fun `deleteState should delete the state and log the deletion`() = runTest {
        val stateId = fakeTaskState.id

        manageStateUseCase.deleteState(stateId)

        coVerify { taskStatesRepository.deleteTaskState(stateId) }
        coVerify { createLogUseCase.logEntityDeletion(stateId) }
    }

    @Test
    fun `getTaskStatesByProjectId should return all states for a project`() = runTest {
        val projectId = UUID.randomUUID()
        val states = listOf(fakeTaskState)
        coEvery { taskStatesRepository.getTaskStatesByProjectId(projectId) } returns states

        val result = manageStateUseCase.getTaskStatesByProjectId(projectId)

        assertEquals(states, result)
    }

    @Test
    fun `getTaskStatesById should return the correct state`() = runTest {
        val stateId = fakeTaskState.id
        coEvery { taskStatesRepository.getTaskStateById(stateId) } returns fakeTaskState

        val result = manageStateUseCase.getTaskStatesById(stateId)

        assertEquals(fakeTaskState, result)
    }

    private companion object {
        val fakeTaskState = TaskState(
            id = UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
            title = "Fake State",
            description = "This is a fake state",
            projectId = UUID.fromString("123e4567-e89b-12d3-a456-426614174001")
        )
    }
}