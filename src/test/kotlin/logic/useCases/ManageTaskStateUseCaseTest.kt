package logic.useCases

import io.mockk.*
import kotlinx.coroutines.test.runTest
import logic.repositories.TaskStatesRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ui.taskManagementView.FakeProjectData
import java.util.*

class ManageTaskStateUseCaseTest {

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
    fun `addState should call taskStatesRepository addNewTaskState when given valid state and project ID`() = runTest{
        mockkStatic(UUID::class)
        every { UUID.randomUUID() } returns FakeProjectData.taskStatesLists[0].id

        useCase.addState(
            FakeProjectData.taskStatesLists[0].title,
            FakeProjectData.taskStatesLists[0].description,
            FakeProjectData.taskStatesLists[0].projectId
        )

        coVerify(exactly = 1) { taskStatesRepo.addNewTaskState(FakeProjectData.taskStatesLists[0], FakeProjectData.taskStatesLists[0].projectId) }
    }

    @Test
    fun `addState should call createLogUseCase logEntityCreation when the state is added` ()= runTest  {
        mockkStatic(UUID::class)
        every { UUID.randomUUID() } returns FakeProjectData.taskStatesLists[0].id

        useCase.addState(
            FakeProjectData.taskStatesLists[0].title,
            FakeProjectData.taskStatesLists[0].description,
            FakeProjectData.taskStatesLists[0].projectId
        )

        coVerify(exactly = 1) { createLogUseCase.logEntityCreation(FakeProjectData.taskStatesLists[0].id) }
    }

    @Test
    fun `editStateTitle should call taskStatesRepository editTaskStateTitle when given a new title`()= runTest {
        val newTitle = "state new title"
        coEvery { taskStatesRepo.getTaskStateById(FakeProjectData.taskStatesLists[0].id) } returns FakeProjectData.taskStatesLists[0]

        useCase.editStateTitle(FakeProjectData.taskStatesLists[0].id, newTitle)

        coVerify(exactly = 1) { taskStatesRepo.editTaskStateTitle(FakeProjectData.taskStatesLists[0].id, newTitle) }
    }

    @Test
    fun `editStateTitle should call createLogUseCase logEntityTitleEdition when edit the title`()= runTest {
        val newTitle = "state new title"
        coEvery { taskStatesRepo.getTaskStateById(FakeProjectData.taskStatesLists[0].id) } returns FakeProjectData.taskStatesLists[0]

        useCase.editStateTitle(FakeProjectData.taskStatesLists[0].id, newTitle)

        coVerify(exactly = 1) {
            createLogUseCase.logEntityTitleEdition(
                FakeProjectData.taskStatesLists[0].id,
                FakeProjectData.taskStatesLists[0].title,
                newTitle
            )
        }
    }

    @Test
    fun `editStateDescription should call taskStatesRepository editTaskStateDescription when given a new description`()  = runTest{
        val newDesc = "state new description"
        coEvery { taskStatesRepo.getTaskStateById(FakeProjectData.taskStatesLists[0].id) } returns FakeProjectData.taskStatesLists[0]

        useCase.editStateDescription(FakeProjectData.taskStatesLists[0].id, newDesc)

        coVerify(exactly = 1) { taskStatesRepo.editTaskStateDescription(FakeProjectData.taskStatesLists[0].id, newDesc) }
    }

    @Test
    fun `editStateDescription should call createLogUseCase logEntityDescriptionEdition when edit the description`()  = runTest{
        val newDesc = "state new description"
        coEvery { taskStatesRepo.getTaskStateById(FakeProjectData.taskStatesLists[0].id) } returns FakeProjectData.taskStatesLists[0]

        useCase.editStateDescription(FakeProjectData.taskStatesLists[0].id, newDesc)

        coVerify(exactly = 1) {
            createLogUseCase.logEntityDescriptionEdition(
                FakeProjectData.taskStatesLists[0].id,
                FakeProjectData.taskStatesLists[0].description,
                newDesc
            )
        }
    }

    @Test
    fun `deleteState should call taskStatesRepository deleteTaskState when state is found`() = runTest{
        coEvery { taskStatesRepo.getTaskStateById(FakeProjectData.taskStatesLists[0].id) } returns FakeProjectData.taskStatesLists[0]

        useCase.deleteState(FakeProjectData.taskStatesLists[0].id)

        coVerify(exactly = 1) { taskStatesRepo.deleteTaskState(FakeProjectData.taskStatesLists[0].id) }
    }
}