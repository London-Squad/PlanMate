package logic.useCases

import logic.entities.*
import logic.repositories.LogsRepository
import logic.repositories.StatesRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.*

class ManageStateUseCaseTest {

    private lateinit var useCase: ManageStateUseCase
    private lateinit var statesRepo: FakeStatesRepository
    private lateinit var logsRepo: FakeLogsRepository
    private val projectId = UUID.randomUUID()
    private val admin = User(UUID.randomUUID(), "admin", User.Type.ADMIN)

    @BeforeEach
    fun setup() {
        statesRepo = FakeStatesRepository()
        logsRepo = FakeLogsRepository()
        useCase = ManageStateUseCase(statesRepo, logsRepo, admin)
    }

    @Test
    fun `should add state and log create action when given valid state and project ID`() {
        val state = State(title = "New", description = "To do")
        useCase.addState(state, projectId)

        val saved = statesRepo.getStatesByProjectId(projectId)
        assertEquals(1, saved.size)
        assertEquals(state.title, saved.first().title)
        assertTrue(logsRepo.logs.any { it.action is Create })
    }

    @Test
    fun `should edit state title and log edit action when state exists`() {
        val state = State(title = "Old", description = "desc")
        statesRepo.addNewState(state, projectId)
        useCase.editStateTitle(state.id, "Updated")

        assertEquals("Updated", statesRepo.getStateById(state.id)?.title)
        assertTrue(logsRepo.logs.any { it.action is Edit })
    }

    @Test
    fun `should edit state description and log edit action when state exists`() {
        val state = State(title = "Old", description = "desc")
        statesRepo.addNewState(state, projectId)
        useCase.editStateDescription(state.id, "New desc")

        assertEquals("New desc", statesRepo.getStateById(state.id)?.description)
        assertTrue(logsRepo.logs.any { it.action is Edit })
    }

    @Test
    fun `should delete state and log delete action when state exists`() {
        val state = State(title = "Delete", description = "desc")
        statesRepo.addNewState(state, projectId)
        useCase.deleteState(state.id)

        assertNull(statesRepo.getStateById(state.id))
        assertTrue(logsRepo.logs.any { it.action is Delete })
    }

    @Test
    fun `should not add state or log when title is blank`() {
        val state = State(title = "", description = "desc")
        useCase.addState(state, projectId)

        assertTrue(statesRepo.getStatesByProjectId(projectId).isEmpty())
        assertTrue(logsRepo.logs.none { it.action is Create })
    }

    @Test
    fun `should not edit title or log when state does not exist`() {
        val unknownId = UUID.randomUUID()
        useCase.editStateTitle(unknownId, "New")

        assertTrue(logsRepo.logs.none { it.action is Edit })
    }

    @Test
    fun `should not edit description or log when state does not exist`() {
        val unknownId = UUID.randomUUID()
        useCase.editStateDescription(unknownId, "Updated")

        assertTrue(logsRepo.logs.none { it.action is Edit })
    }

    @Test
    fun `should not edit title or log when new title is blank`() {
        val state = State(title = "Real", description = "desc")
        statesRepo.addNewState(state, projectId)
        useCase.editStateTitle(state.id, "")

        assertEquals("Real", statesRepo.getStateById(state.id)?.title)
        assertTrue(logsRepo.logs.none { it.action is Edit })
    }

    @Test
    fun `should not edit description or log when new description is blank`() {
        val state = State(title = "Real", description = "desc")
        statesRepo.addNewState(state, projectId)
        useCase.editStateDescription(state.id, "")

        assertEquals("desc", statesRepo.getStateById(state.id)?.description)
        assertTrue(logsRepo.logs.none { it.action is Edit })
    }

    @Test
    fun `should not delete or log when state does not exist`() {
        val unknownId = UUID.randomUUID()
        useCase.deleteState(unknownId)
        assertTrue(logsRepo.logs.none { it.action is Delete })
    }

    @Test
    fun should_not_delete_or_log_when_state_is_NoState() {
        val noState = State.NoState
        statesRepo.addNewState(noState, projectId)
        useCase.deleteState(noState.id)

        assertNotNull(statesRepo.getStateById(noState.id))
        assertTrue(logsRepo.logs.none { it.action is Delete })
    }

    // Fake implementations
    class FakeStatesRepository : StatesRepository {
        private val states = mutableMapOf<UUID, State>()
        private val projectStates = mutableMapOf<UUID, MutableList<UUID>>()

        override fun addNewState(state: State, projectId: UUID) {
            states[state.id] = state
            projectStates.getOrPut(projectId) { mutableListOf() }.add(state.id)
        }

        override fun editStateTitle(stateId: UUID, newTitle: String) {
            states[stateId]?.let { states[stateId] = it.copy(title = newTitle) }
        }

        override fun editStateDescription(stateId: UUID, newDescription: String) {
            states[stateId]?.let { states[stateId] = it.copy(description = newDescription) }
        }

        override fun deleteState(stateId: UUID) {
            states.remove(stateId)
            projectStates.values.forEach { it.remove(stateId) }
        }

        override fun getStateById(stateId: UUID): State? = states[stateId]

        override fun getStatesByProjectId(projectId: UUID): List<State> {
            return projectStates[projectId]?.mapNotNull { states[it] } ?: emptyList()
        }
    }

    class FakeLogsRepository : LogsRepository {
        val logs = mutableListOf<Log>()

        override fun addLog(log: Log) {
            logs.add(log)
        }

        override fun getAllLogs(): List<Log> = logs

        override fun getLogById(id: UUID): List<Log> = logs.filter { it.id == id }
    }
}