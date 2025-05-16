package logic.useCases

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import logic.entities.Log
import logic.entities.Log.EntityType
import logic.exceptions.NotFoundException
import logic.repositories.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals

class GetLogsDetailsUseCaseTest {

    private lateinit var logsRepository: LogsRepository
    private lateinit var taskStateRepository: TaskStatesRepository
    private lateinit var taskRepository: TaskRepository
    private lateinit var projectRepository: ProjectsRepository
    private lateinit var userRepository: UserRepository
    private lateinit var getLogsDetailsUseCase: GetLogsDetailsUseCase

    @BeforeEach
    fun setUp() {
        logsRepository = mockk(relaxed = true)
        taskStateRepository = mockk(relaxed = true)
        taskRepository = mockk(relaxed = true)
        projectRepository = mockk(relaxed = true)
        userRepository = mockk(relaxed = true)
        getLogsDetailsUseCase = GetLogsDetailsUseCase(
            logsRepository,
            taskStateRepository,
            taskRepository,
            projectRepository,
            userRepository
        )
    }

    @Test
    fun `getLogsByEntityId should return logs for project with related tasks and states`() = runTest {
        val projectId = fakeData.fakeProject.id
        val logs = listOf(fakeData.fakeLog)
        coEvery { projectRepository.getProjectById(projectId) } returns fakeData.fakeProject
        coEvery { taskRepository.getTasksByProjectID(projectId, includeDeleted = true) } returns listOf(fakeData.fakeTask)
        coEvery { taskStateRepository.getTaskStatesByProjectId(projectId, includeDeleted = true) } returns listOf(fakeData.fakeTaskState)
        coEvery { logsRepository.getLogsByEntitiesIds(any()) } returns logs

        val result = getLogsDetailsUseCase.getLogsByEntityId(projectId)

        assertEquals(logs, result)
    }

    @Test
    fun `getLogsByEntityId should return logs only for entityId when project not found`() = runTest {
        val projectId = fakeData.fakeProject.id
        val logs = listOf(fakeData.fakeLog)
        coEvery { projectRepository.getProjectById(projectId) } throws NotFoundException("Project not found")
        coEvery { logsRepository.getLogsByEntitiesIds(setOf(projectId) as MutableSet<UUID>) } returns logs

        val result = getLogsDetailsUseCase.getLogsByEntityId(projectId)

        assertEquals(logs, result)
    }

    @Test
    fun `getEntityTitleById should return task state title`() = runTest {
        val taskStateId = fakeData.fakeTaskState.id
        val expectedTitle = "Fake Task State"
        coEvery { taskStateRepository.getTaskStateTitleById(taskStateId) } returns expectedTitle

        val result = getLogsDetailsUseCase.getEntityTitleById(taskStateId, EntityType.TASK_STATE)

        assertEquals(expectedTitle, result)
    }

    @Test
    fun `getEntityTitleById should return task title`() = runTest {
        val taskId = fakeData.fakeTask.id
        val expectedTitle = "Fake Task"
        coEvery { taskRepository.getTaskTitleById(taskId) } returns expectedTitle

        val result = getLogsDetailsUseCase.getEntityTitleById(taskId, EntityType.TASK)

        assertEquals(expectedTitle, result)
    }

    @Test
    fun `getEntityTitleById should return project title`() = runTest {
        val projectId = fakeData.fakeProject.id
        val expectedTitle = "Fake Project"
        coEvery { projectRepository.getProjectTitleById(projectId) } returns expectedTitle

        val result = getLogsDetailsUseCase.getEntityTitleById(projectId, EntityType.PROJECT)

        assertEquals(expectedTitle, result)
    }

    @Test
    fun `getEntityTitleById should return user name`() = runTest {
        val userId = fakeData.mate.id
        val expectedUserName = "fakeMate"
        coEvery { userRepository.getUserNameById(userId) } returns expectedUserName

        val result = getLogsDetailsUseCase.getEntityTitleById(userId, EntityType.USER)

        assertEquals(expectedUserName, result)
    }
}
