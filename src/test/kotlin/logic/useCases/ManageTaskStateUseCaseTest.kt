package logic.useCases

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import logic.entities.*
import logic.repositories.TaskStatesRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ui.taskManagementView.FakeProjectData
import java.util.*

class ManageTaskStateUseCaseTest {

    private lateinit var useCase: ManageStateUseCase
    private lateinit var taskStatesRepo: TaskStatesRepository
    private lateinit var createLogUseCase: CreateLogUseCase
    private val projectId = UUID.randomUUID()

    @BeforeEach
    fun setup() {
        taskStatesRepo = mockk(relaxed = true)
        createLogUseCase = mockk(relaxed = true)

        useCase = ManageStateUseCase(taskStatesRepo, createLogUseCase)
    }

    @Test
    fun `addState should call taskStatesRepository addNewTaskState when given valid state and project ID`() {
        mockkStatic(UUID::class)
        every { UUID.randomUUID() } returns FakeProjectData.taskStatesLists[0].id

        useCase.addState(
            FakeProjectData.taskStatesLists[0].title,
            FakeProjectData.taskStatesLists[0].description,
            projectId
        )

        verify(exactly = 1) { taskStatesRepo.addNewTaskState(FakeProjectData.taskStatesLists[0], projectId) }
    }

    @Test
    fun `addState should call createLogUseCase logEntityCreation when the state is added`() {
        mockkStatic(UUID::class)
        every { UUID.randomUUID() } returns FakeProjectData.taskStatesLists[0].id

        useCase.addState(
            FakeProjectData.taskStatesLists[0].title,
            FakeProjectData.taskStatesLists[0].description,
            projectId
        )

        verify(exactly = 1) { createLogUseCase.logEntityCreation(FakeProjectData.taskStatesLists[0]) }
    }

    @Test
    fun `editStateTitle should call taskStatesRepository editTaskStateTitle when given a new title`() {
        val newTitle = "state new title"
        every { taskStatesRepo.getTaskStateById(FakeProjectData.taskStatesLists[0].id) } returns FakeProjectData.taskStatesLists[0]

        useCase.editStateTitle(FakeProjectData.taskStatesLists[0].id, newTitle)

        verify(exactly = 1) { taskStatesRepo.editTaskStateTitle(FakeProjectData.taskStatesLists[0].id, newTitle) }
    }

    @Test
    fun `editStateTitle should call createLogUseCase logEntityTitleEdition when edit the title`() {
        val newTitle = "state new title"
        every { taskStatesRepo.getTaskStateById(FakeProjectData.taskStatesLists[0].id) } returns FakeProjectData.taskStatesLists[0]

        useCase.editStateTitle(FakeProjectData.taskStatesLists[0].id, newTitle)

        verify(exactly = 1) {
            createLogUseCase.logEntityTitleEdition(
                FakeProjectData.taskStatesLists[0],
                FakeProjectData.taskStatesLists[0].title,
                newTitle
            )
        }
    }

    @Test
    fun `editStateDescription should call taskStatesRepository editTaskStateDescription when given a new description`() {
        val newDesc = "state new description"
        every { taskStatesRepo.getTaskStateById(FakeProjectData.taskStatesLists[0].id) } returns FakeProjectData.taskStatesLists[0]

        useCase.editStateDescription(FakeProjectData.taskStatesLists[0].id, newDesc)

        verify(exactly = 1) { taskStatesRepo.editTaskStateDescription(FakeProjectData.taskStatesLists[0].id, newDesc) }
    }

    @Test
    fun `editStateDescription should call createLogUseCase logEntityDescriptionEdition when edit the description`() {
        val newDesc = "state new description"
        every { taskStatesRepo.getTaskStateById(FakeProjectData.taskStatesLists[0].id) } returns FakeProjectData.taskStatesLists[0]

        useCase.editStateDescription(FakeProjectData.taskStatesLists[0].id, newDesc)

        verify(exactly = 1) {
            createLogUseCase.logEntityDescriptionEdition(
                FakeProjectData.taskStatesLists[0],
                FakeProjectData.taskStatesLists[0].description,
                newDesc
            )
        }
    }

    @Test
    fun `deleteState should call taskStatesRepository deleteTaskState when state is found and it is not No State`() {
        every { taskStatesRepo.getTaskStateById(FakeProjectData.taskStatesLists[0].id) } returns FakeProjectData.taskStatesLists[0]

        useCase.deleteState(FakeProjectData.taskStatesLists[0].id)

        verify(exactly = 1) { taskStatesRepo.deleteTaskState(FakeProjectData.taskStatesLists[0].id) }
    }

    @Test
    fun `deleteState should  not call taskStatesRepository deleteTaskState when state is No State`() {
        val noTaskState = TaskState.NoTaskState
        every { taskStatesRepo.getTaskStateById(FakeProjectData.taskStatesLists[0].id) } returns noTaskState

        useCase.deleteState(noTaskState.id)

        verify(exactly = 0) { taskStatesRepo.deleteTaskState(FakeProjectData.taskStatesLists[0].id) }
    }
}