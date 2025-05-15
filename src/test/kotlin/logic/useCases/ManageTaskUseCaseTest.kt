package logic.useCases

import com.google.common.truth.Truth.assertThat
import io.mockk.*
import kotlinx.coroutines.test.runTest
import logic.entities.Task
import logic.repositories.TaskRepository
import logic.repositories.TaskStatesRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ui.taskManagementView.FakeProjectData
import java.util.*

class ManageTaskUseCaseTest {

    private lateinit var manageTaskUseCase: ManageTaskUseCase

    private lateinit var taskRepository: TaskRepository
    private lateinit var createLogUseCase: CreateLogUseCase
    private lateinit var taskStatesRepository: TaskStatesRepository

    @BeforeEach
    fun setup() {
        taskRepository = mockk(relaxed = true)
        createLogUseCase = mockk(relaxed = true)
        taskStatesRepository = mockk(relaxed = true)

        manageTaskUseCase = ManageTaskUseCase(taskRepository, createLogUseCase, taskStatesRepository)
    }

    @Test
    fun `getTaskByID should return the task when the task is found`() = runTest {
        val task = FakeProjectData.tasksList[0]
        coEvery { taskRepository.getTaskByID(task.id) } returns task

        val result = manageTaskUseCase.getTaskByID(task.id)

        assertThat(result).isEqualTo(task)
    }

    @Test
    fun `addNewTask should call taskRepository addNewTask`() = runTest {
        val task = Task(
            id = UUID.randomUUID(), title = "title", description = "",
            taskStateId = FakeProjectData.taskStatesList.first().id
        )
        mockkStatic(UUID::class)
        every { UUID.randomUUID() } returns task.id
        coEvery { taskStatesRepository.getTaskStatesByProjectId(FakeProjectData.project.id) } returns FakeProjectData.taskStatesList

        manageTaskUseCase.addNewTask(
            task.title,
            task.description,
            FakeProjectData.project.id
        )

        coVerify(exactly = 1) { taskRepository.addNewTask(task, FakeProjectData.project.id) }
    }

    @Test
    fun `editTaskTitle should call taskRepository editTaskTitle`() = runTest {
        val taskId = FakeProjectData.tasksList[0].id
        val title = "new title"

        manageTaskUseCase.editTaskTitle(taskId, title)

        coVerify(exactly = 1) { taskRepository.editTaskTitle(taskId, title) }
    }

    @Test
    fun `editTaskTitle should call createLogUseCase logEntityTitleEdition when edit the title`() = runTest {
        val task = FakeProjectData.tasksList[0]
        val newTitle = "new title"
        coEvery { taskRepository.getTaskByID(task.id) } returns task

        manageTaskUseCase.editTaskTitle(task.id, newTitle)

        coVerify(exactly = 1) { createLogUseCase.logEntityTitleEdition(task.id, task.title, newTitle) }
    }

    @Test
    fun `editTaskDescription should call taskRepository editTaskDescription`() = runTest {
        val taskId = FakeProjectData.tasksList[0].id
        val description = "new description"
        manageTaskUseCase.editTaskDescription(taskId, description)

        coVerify(exactly = 1) { taskRepository.editTaskDescription(taskId, description) }
    }

    @Test
    fun `editTaskDescription should call createLogUseCase logEntityDescriptionEdition when edit the description`() =
        runTest {
            val task = FakeProjectData.tasksList[0]
            val newDescription = "new description"
            coEvery { taskRepository.getTaskByID(task.id) } returns task

            manageTaskUseCase.editTaskDescription(task.id, newDescription)

            coVerify(exactly = 1) {
                createLogUseCase.logEntityDescriptionEdition(
                    task.id,
                    task.description,
                    newDescription
                )
            }
        }

    @Test
    fun `editTaskState should call taskRepository editTaskState`() = runTest {
        val stateId = FakeProjectData.taskStatesList[0].id

        manageTaskUseCase.editTaskState(FakeProjectData.tasksList[0].id, stateId)

        coVerify(exactly = 1) { taskRepository.editTaskState(FakeProjectData.tasksList[0].id, stateId) }
    }

    @Test
    fun `editTaskState should call createLogUseCase editTaskState when edit the task state`() =
        runTest {
            val task = FakeProjectData.tasksList[0]
            val oldState = FakeProjectData.taskStatesList[0]
            val newState = FakeProjectData.taskStatesList[1]
            coEvery { taskRepository.getTaskByID(task.id) } returns task
            coEvery { taskStatesRepository.getTaskStateById(newState.id) } returns newState
            coEvery { taskStatesRepository.getTaskStateById(oldState.id) } returns oldState

            manageTaskUseCase.editTaskState(task.id, newState.id)

            coVerify(exactly = 1) {
                createLogUseCase.logTaskStateEdition(
                    task.id,
                    oldState.title,
                    newState.title
                )
            }
        }

    @Test
    fun `deleteTask should call taskRepository deleteTask`() = runTest {
        val taskId = FakeProjectData.tasksList[0].id
        manageTaskUseCase.deleteTask(taskId)

        coVerify(exactly = 1) { taskRepository.deleteTask(taskId) }
    }

    @Test
    fun `deleteTask should call createLogUseCase logEntityDeletion when delete the task`() = runTest {
        val taskId = FakeProjectData.tasksList[0].id

        manageTaskUseCase.deleteTask(taskId)

        coVerify(exactly = 1) { createLogUseCase.logEntityDeletion(taskId) }
    }
}