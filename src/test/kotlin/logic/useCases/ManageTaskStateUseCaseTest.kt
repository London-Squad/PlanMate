//package logic.useCases
//
//import io.mockk.every
//import io.mockk.mockk
//import logic.entities.*
//import logic.repositories.AuthenticationRepository
//import logic.repositories.LogsRepository
//import logic.repositories.TasksStatesRepository
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.Test
//import java.util.*
//import kotlin.test.assertEquals
//import kotlin.test.assertNotNull
//import kotlin.test.assertTrue
//
//class ManageTaskStateUseCaseTest {
//
//    private lateinit var useCase: ManageStateUseCase
//    private lateinit var statesRepo: FakeTasksStatesRepository
//    private lateinit var logsRepo: FakeLogsRepository
//    private lateinit var authenticationRepository: AuthenticationRepository
//    private val projectId = UUID.randomUUID()
//    private val admin = User(UUID.randomUUID(), "admin", User.Type.ADMIN)
//
//    @BeforeEach
//    fun setup() {
//        statesRepo = FakeTasksStatesRepository()
//        logsRepo = FakeLogsRepository()
//
//        authenticationRepository = mockk(relaxed = true)
//        every { authenticationRepository.getLoggedInUser() } returns admin
//
//        useCase = ManageStateUseCase(statesRepo, logsRepo, authenticationRepository)
//    }
//
//    @Test
//    fun `should add state and log create action when given valid state and project ID`() {
//        val taskState = TaskState(title = "New", description = "To do")
//        useCase.addState(taskState, projectId)
//
//        val saved = statesRepo.getTasksStatesByProjectId(projectId)
//        assertEquals(1, saved.size)
//        assertEquals(taskState.title, saved.first().title)
//        assertTrue(logsRepo.logs.any { it.action is Create })
//    }
//
//    @Test
//    fun `should edit state title and log edit action when state exists`() {
//        val taskState = TaskState(title = "Old", description = "desc")
//        statesRepo.addNewTaskState(taskState, projectId)
//        useCase.editStateTitle(taskState.id, "Updated")
//
//        assertEquals("Updated", statesRepo.getStateById(taskState.id)?.title)
//        assertTrue(logsRepo.logs.any { it.action is Edit })
//    }
//
//    @Test
//    fun `should edit state description and log edit action when state exists`() {
//        val taskState = TaskState(title = "Old", description = "desc")
//        statesRepo.addNewTaskState(taskState, projectId)
//        useCase.editStateDescription(taskState.id, "New desc")
//
//        assertEquals("New desc", statesRepo.getStateById(taskState.id)?.description)
//        assertTrue(logsRepo.logs.any { it.action is Edit })
//    }
//
//    @Test
//    fun `should not add state or log when title is blank`() {
//        val taskState = TaskState(title = "", description = "desc")
//        useCase.addState(taskState, projectId)
//
//        assertTrue(statesRepo.getTasksStatesByProjectId(projectId).isEmpty())
//        assertTrue(logsRepo.logs.none { it.action is Create })
//    }
//
//    @Test
//    fun `should not edit title or log when new title is blank`() {
//        val taskState = TaskState(title = "Real", description = "desc")
//        statesRepo.addNewTaskState(taskState, projectId)
//        useCase.editStateTitle(taskState.id, "")
//
//        assertEquals("Real", statesRepo.getStateById(taskState.id)?.title)
//        assertTrue(logsRepo.logs.none { it.action is Edit })
//    }
//
//    @Test
//    fun `should not edit description or log when new description is blank`() {
//        val taskState = TaskState(title = "Real", description = "desc")
//        statesRepo.addNewTaskState(taskState, projectId)
//        useCase.editStateDescription(taskState.id, "")
//
//        assertEquals("desc", statesRepo.getStateById(taskState.id)?.description)
//        assertTrue(logsRepo.logs.none { it.action is Edit })
//    }
//
//    @Test
//    fun `should not delete or log when state does not exist`() {
//        val unknownId = UUID.randomUUID()
//        useCase.deleteState(unknownId)
//        assertTrue(logsRepo.logs.none { it.action is Delete })
//    }
//
//    @Test
//    fun should_not_delete_or_log_when_state_is_NoState() {
//        val noTaskState = TaskState.NoTaskState
//        statesRepo.addNewTaskState(noTaskState, projectId)
//        useCase.deleteState(noTaskState.id)
//
//        assertNotNull(statesRepo.getStateById(noTaskState.id))
//        assertTrue(logsRepo.logs.none { it.action is Delete })
//    }
//
//    // Fake implementations
//    class FakeTasksStatesRepository : TasksStatesRepository {
//        private val states = mutableMapOf<UUID, TaskState>()
//        private val projectStates = mutableMapOf<UUID, MutableList<UUID>>()
//
//        override fun addNewTaskState(taskState: TaskState, projectId: UUID) {
//            states[taskState.id] = taskState
//            projectStates.getOrPut(projectId) { mutableListOf() }.add(taskState.id)
//        }
//
//        override fun editTaskStateTitle(stateId: UUID, newTitle: String) {
//            states[stateId]?.let { states[stateId] = it.copy(title = newTitle) }
//        }
//
//        override fun editTaskStateDescription(stateId: UUID, newDescription: String) {
//            states[stateId]?.let { states[stateId] = it.copy(description = newDescription) }
//        }
//
//        override fun deleteTaskState(stateId: UUID) {
//            states.remove(stateId)
//            projectStates.values.forEach { it.remove(stateId) }
//        }
//
//        override fun getTasksStatesByProjectId(projectId: UUID): List<TaskState> {
//            return projectStates[projectId]?.mapNotNull { states[it] } ?: emptyList()
//        }
//
//        override fun getStateById(stateId: UUID): TaskState {
//            return states[stateId] ?: TaskState.NoTaskState
//        }
//    }
//
//    class FakeLogsRepository : LogsRepository {
//        val logs = mutableListOf<Log>()
//
//        override fun addLog(log: Log) {
//            logs.add(log)
//        }
//
//        override fun getAllLogs(): List<Log> = logs
//        override fun getLogsByEntityId(entityId: UUID): List<Log> = logs.filter { it.id == entityId }
//
//    }
//}