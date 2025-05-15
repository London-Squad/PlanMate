package logic.useCases

import com.google.common.truth.Truth.assertThat
import io.mockk.*
import kotlinx.coroutines.test.runTest
import logic.repositories.TaskStatesRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ui.taskManagementView.FakeProjectData
import java.util.*

class ManageStateUseCaseTest {

    private lateinit var useCase: ManageStateUseCase
    private lateinit var taskStatesRepo: TaskStatesRepository
    private lateinit var createLogUseCase: CreateLogUseCase

    @BeforeEach
    fun setup() {
        taskStatesRepo = mockk(relaxed = true)
        createLogUseCase = mockk(relaxed = true)

        useCase = ManageStateUseCase(taskStatesRepo, createLogUseCase)
    }

    @Test
    fun `addState should call taskStatesRepository addNewTaskState when given valid state and project ID`() = runTest {
        mockkStatic(UUID::class)
        every { UUID.randomUUID() } returns FakeProjectData.taskStatesList[0].id

        useCase.addState(
            FakeProjectData.taskStatesList[0].title,
            FakeProjectData.taskStatesList[0].description,
            FakeProjectData.taskStatesList[0].projectId
        )

        coVerify(exactly = 1) {
            taskStatesRepo.addNewTaskState(
                FakeProjectData.taskStatesList[0],
                FakeProjectData.taskStatesList[0].projectId
            )
        }
    }

    @Test
    fun `addState should call createLogUseCase logEntityCreation when the state is added`() = runTest {
        mockkStatic(UUID::class)
        every { UUID.randomUUID() } returns FakeProjectData.taskStatesList[0].id

        useCase.addState(
            FakeProjectData.taskStatesList[0].title,
            FakeProjectData.taskStatesList[0].description,
            FakeProjectData.taskStatesList[0].projectId
        )

        coVerify(exactly = 1) { createLogUseCase.logEntityCreation(FakeProjectData.taskStatesList[0].id) }
    }

    @Test
    fun `editStateTitle should call taskStatesRepository editTaskStateTitle when given a new title`() = runTest {
        val newTitle = "state new title"
        coEvery { taskStatesRepo.getTaskStateById(FakeProjectData.taskStatesList[0].id) } returns FakeProjectData.taskStatesList[0]

        useCase.editStateTitle(FakeProjectData.taskStatesList[0].id, newTitle)

        coVerify(exactly = 1) { taskStatesRepo.editTaskStateTitle(FakeProjectData.taskStatesList[0].id, newTitle) }
    }

    @Test
    fun `editStateTitle should call createLogUseCase logEntityTitleEdition when edit the title`() = runTest {
        val newTitle = "state new title"
        coEvery { taskStatesRepo.getTaskStateById(FakeProjectData.taskStatesList[0].id) } returns FakeProjectData.taskStatesList[0]

        useCase.editStateTitle(FakeProjectData.taskStatesList[0].id, newTitle)

        coVerify(exactly = 1) {
            createLogUseCase.logEntityTitleEdition(
                FakeProjectData.taskStatesList[0].id,
                FakeProjectData.taskStatesList[0].title,
                newTitle
            )
        }
    }

    @Test
    fun `editStateDescription should call taskStatesRepository editTaskStateDescription when given a new description`() =
        runTest {
            val newDesc = "state new description"
            coEvery { taskStatesRepo.getTaskStateById(FakeProjectData.taskStatesList[0].id) } returns FakeProjectData.taskStatesList[0]

            useCase.editStateDescription(FakeProjectData.taskStatesList[0].id, newDesc)

            coVerify(exactly = 1) {
                taskStatesRepo.editTaskStateDescription(
                    FakeProjectData.taskStatesList[0].id,
                    newDesc
                )
            }
        }

    @Test
    fun `editStateDescription should call createLogUseCase logEntityDescriptionEdition when edit the description`() =
        runTest {
            val newDesc = "state new description"
            coEvery { taskStatesRepo.getTaskStateById(FakeProjectData.taskStatesList[0].id) } returns FakeProjectData.taskStatesList[0]

            useCase.editStateDescription(FakeProjectData.taskStatesList[0].id, newDesc)

            coVerify(exactly = 1) {
                createLogUseCase.logEntityDescriptionEdition(
                    FakeProjectData.taskStatesList[0].id,
                    FakeProjectData.taskStatesList[0].description,
                    newDesc
                )
            }
        }

    @Test
    fun `deleteState should call taskStatesRepository deleteTaskState when state is found`() = runTest {
        coEvery { taskStatesRepo.getTaskStateById(FakeProjectData.taskStatesList[0].id) } returns FakeProjectData.taskStatesList[0]

        useCase.deleteState(FakeProjectData.taskStatesList[0].id)

        coVerify(exactly = 1) { taskStatesRepo.deleteTaskState(FakeProjectData.taskStatesList[0].id) }
    }

    @Test
    fun `getTaskStatesByProjectId should return all states for a project`() = runTest {
        val projectId = UUID.randomUUID()
        coEvery { taskStatesRepo.getTaskStatesByProjectId(projectId) } returns FakeProjectData.taskStatesList

        val result = useCase.getTaskStatesByProjectId(projectId)

        assertThat(result).isEqualTo(FakeProjectData.taskStatesList)
    }

    @Test
    fun `getTaskStatesById should return the correct state`() = runTest {
        val state = FakeProjectData.taskStatesList[0]
        coEvery { taskStatesRepo.getTaskStateById(state.id) } returns state

        val result = useCase.getTaskStatesById(state.id)

        assertThat(result).isEqualTo(state)
    }
}