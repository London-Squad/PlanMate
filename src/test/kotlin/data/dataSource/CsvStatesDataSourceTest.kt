package data.dataSource

import data.csvStorage.CsvStatesDataSource
import logic.entities.State
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import java.util.UUID
import kotlin.test.assertEquals

class CsvStatesDataSourceTest {

    private lateinit var file: File
    private lateinit var csvStatesDataSource: CsvStatesDataSource
    private val projectId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000")
    private val stateId = UUID.fromString("07fa94e1-8030-41ad-8342-fefa210461ce")

    @BeforeEach
    fun setUp() {
        file = File.createTempFile("states", ".csv")
        file.writeText("id,title,description,projectId\n")
        csvStatesDataSource = CsvStatesDataSource(file)
    }

    @AfterEach
    fun tearDown() {
        file.delete()
    }

    @Test
    fun `should return empty list when no states for project`() {
        // When
        val states = csvStatesDataSource.getAllStatesByProjectId(projectId)

        // Then
        assertEquals(0, states.size)
    }

    @Test
    fun `should return states for matching projectId`() {
        // Given
        file.appendText("$stateId,No State,this is the default state,$projectId\n")

        // When
        val states = csvStatesDataSource.getAllStatesByProjectId(projectId)

        // Then
        assertEquals(1, states.size)
        assertEquals("No State", states[0].title)
    }

    @Test
    fun `should return empty list when states for other projectId`() {
        // Given
        file.appendText("$stateId,No State,this is the default state,${UUID.randomUUID()}\n")

        // When
        val states = csvStatesDataSource.getAllStatesByProjectId(projectId)

        // Then
        assertEquals(0, states.size)
    }

    @Test
    fun `should return StateNoState when state not found`() {
        // Given
        // File is initialized with only header

        // When
        val state = csvStatesDataSource.getStateById(stateId)

        // Then
        assertEquals(State.NoState.title, state.title)
    }

    @Test
    fun `should return correct state when state exists`() {
        // Given
        file.appendText("$stateId,No State,this is the default state,$projectId\n")

        // When
        val state = csvStatesDataSource.getStateById(stateId)

        // Then
        assertEquals("No State", state.title)
    }

    @Test
    fun `should add new state to file`() {
        // Given
        val newState = State(stateId, "TODO", "To-do tasks")

        // When
        csvStatesDataSource.addNewState(newState, projectId)

        // Then
        val lines = file.readLines()
        assertEquals(2, lines.size)
        assertEquals("$stateId,TODO,To-do tasks,$projectId", lines[1])
    }

    @Test
    fun `should update state title when editing`() {
        // Given
        file.appendText("$stateId,No State,this is the default state,$projectId\n")
        val newTitle = "In Progress"

        // When
        csvStatesDataSource.editStateTitle(stateId, newTitle)

        // Then
        val lines = file.readLines()
        assertEquals(2, lines.size)
        assertEquals("$stateId,$newTitle,this is the default state,$projectId", lines[1])
    }

    @Test
    fun `should update state description when editing`() {
        // Given
        file.appendText("$stateId,No State,this is the default state,$projectId\n")
        val newDescription = "Tasks in progress"

        // When
        csvStatesDataSource.editStateDescription(stateId, newDescription)

        // Then
        val lines = file.readLines()
        assertEquals(2, lines.size)
        assertEquals("$stateId,No State,$newDescription,$projectId", lines[1])
    }

    @Test
    fun `should delete state from file`() {
        // Given
        file.appendText("$stateId,No State,this is the default state,$projectId\n")

        // When
        csvStatesDataSource.deleteState(stateId)

        // Then
        val lines = file.readLines()
        assertEquals(1, lines.size) // Only header remains
    }
}