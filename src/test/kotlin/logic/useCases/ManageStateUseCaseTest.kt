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
    private lateinit var fakeRepo: FakeStatesRepo
    private lateinit var fakeLogs: FakeLogsRepo
    private lateinit var admin: User

    class FakeStatesRepo : StatesRepository {
        val states = mutableMapOf<UUID, State>()

        override fun addNewState(state: State, projectId: UUID) {
            if (state.title.isNotBlank()) {
                states[state.id] = state
            }
        }

        override fun editStateTitle(stateId: UUID, newTitle: String) {
            states[stateId]?.let {
                if (newTitle.isNotBlank()) {
                    states[stateId] = it.copy(title = newTitle)
                }
            }
        }

        override fun editStateDescription(stateId: UUID, newDescription: String) {
            states[stateId]?.let {
                if (newDescription.isNotBlank()) {
                    states[stateId] = it.copy(description = newDescription)
                }
            }
        }

        override fun deleteState(stateId: UUID) {
            if (stateId != State.NoState.id) {
                states.remove(stateId)
            }
        }


         override fun getStateById(stateId: UUID): State? {
            return states[stateId]
        }
    }

    class FakeLogsRepo : LogsRepository {
        val logs = mutableListOf<Log>()

        override fun getAllLogs() = logs
        override fun getLogById(id: UUID) = logs.filter { it.id == id }
        override fun addLog(log: Log) {
            logs.add(log)
        }
    }

    @BeforeEach
    fun setUp() {
        fakeRepo = FakeStatesRepo()
        fakeLogs = FakeLogsRepo()
        admin = User(userName = "admin", type = User.Type.ADMIN)
        useCase = ManageStateUseCase(fakeRepo, fakeLogs, admin)
    }

    @Test
    fun `should add state and log create action`() {
        val state = State(title = "Test", description = "Desc")
        useCase.addState(state, UUID.randomUUID())
        assertEquals(1, fakeRepo.states.size)
        assertEquals(1, fakeLogs.logs.size)
        assertTrue(fakeLogs.logs.first().action is Create)
    }

    @Test
    fun `should not add state if title is blank`() {
        val state = State(title = "", description = "...")
        useCase.addState(state, UUID.randomUUID())
        assertEquals(0, fakeRepo.states.size)
        assertEquals(0, fakeLogs.logs.size)
    }

    @Test
    fun `should edit title and log edit action`() {
        val state = State(title = "Old", description = "Desc")
        fakeRepo.states[state.id] = state
        useCase.editStateTitle(state.id, "New")
        assertEquals("New", fakeRepo.states[state.id]?.title)
        val log = fakeLogs.logs.first().action as Edit
        assertEquals("title", log.property)
        assertEquals("Old", log.oldValue)
        assertEquals("New", log.newValue)
    }

    @Test
    fun `should not edit title if state does not exist`() {
        useCase.editStateTitle(UUID.randomUUID(), "Any")
        assertEquals(0, fakeLogs.logs.size)
    }

    @Test
    fun `should not edit title if new title is blank`() {
        val state = State(title = "Old", description = "Desc")
        fakeRepo.states[state.id] = state
        useCase.editStateTitle(state.id, "")
        assertEquals("Old", fakeRepo.states[state.id]?.title)
        assertEquals(0, fakeLogs.logs.size)
    }

    @Test
    fun `should edit description and log edit action`() {
        val state = State(title = "X", description = "OldDesc")
        fakeRepo.states[state.id] = state
        useCase.editStateDescription(state.id, "NewDesc")
        assertEquals("NewDesc", fakeRepo.states[state.id]?.description)
        val log = fakeLogs.logs.first().action as Edit
        assertEquals("description", log.property)
        assertEquals("OldDesc", log.oldValue)
        assertEquals("NewDesc", log.newValue)
    }

    @Test
    fun `should not edit description if state does not exist`() {
        useCase.editStateDescription(UUID.randomUUID(), "Any")
        assertEquals(0, fakeLogs.logs.size)
    }

    @Test
    fun `should not edit description if new description is blank`() {
        val state = State(title = "X", description = "Old")
        fakeRepo.states[state.id] = state
        useCase.editStateDescription(state.id, "")
        assertEquals("Old", fakeRepo.states[state.id]?.description)
        assertEquals(0, fakeLogs.logs.size)
    }

    @Test
    fun `should delete state and log delete action`() {
        val state = State(title = "Del", description = "ToDelete")
        fakeRepo.states[state.id] = state
        useCase.deleteState(state.id)
        assertEquals(0, fakeRepo.states.size)
        assertTrue(fakeLogs.logs.first().action is Delete)
    }

    @Test
    fun `should not delete state if it does not exist`() {
        useCase.deleteState(UUID.randomUUID())
        assertEquals(0, fakeLogs.logs.size)
    }

    @Test
    fun `should not delete NoState`() {
        fakeRepo.states[State.NoState.id] = State.NoState
        useCase.deleteState(State.NoState.id)
        assertTrue(fakeRepo.states.contains(State.NoState.id))
        assertEquals(0, fakeLogs.logs.size)
    }

    @Test
    fun `should include correct user in log`() {
        val state = State(title = "Test", description = "...")
        useCase.addState(state, UUID.randomUUID())
        assertEquals(admin.id, fakeLogs.logs.first().user.id)
    }
}