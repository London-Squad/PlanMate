package data.dataSources.mongoDBDataSource

import com.google.common.truth.Truth.assertThat
import data.dataSources.mongoDBDataSource.mongoDBHandler.MongoDBParser
import data.dataSources.mongoDBDataSource.mongoDBHandler.MongoDBParser.Companion.DESCRIPTION_FIELD
import data.dataSources.mongoDBDataSource.mongoDBHandler.MongoDBParser.Companion.ID_FIELD
import data.dataSources.mongoDBDataSource.mongoDBHandler.MongoDBParser.Companion.IS_DELETED_FIELD
import data.dataSources.mongoDBDataSource.mongoDBHandler.MongoDBParser.Companion.PROJECT_ID_FIELD
import data.dataSources.mongoDBDataSource.mongoDBHandler.MongoDBParser.Companion.STATE_ID_FIELD
import data.dataSources.mongoDBDataSource.mongoDBHandler.MongoDBParser.Companion.TITLE_FIELD
import data.dataSources.mongoDBDataSource.mongoDBHandler.MongoDBQueryHandler
import data.dto.TaskDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import logic.entities.Task
import org.bson.Document
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals

class MongoDBTasksDataSourceTest {

    private lateinit var dataSource: MongoDBTasksDataSource

    private lateinit var queryHandler: MongoDBQueryHandler
    private lateinit var parser: MongoDBParser

    @BeforeEach
    fun setUp() {
        queryHandler = mockk(relaxed = true)
        parser = mockk(relaxed = true)

        dataSource = MongoDBTasksDataSource(queryHandler, parser)
    }

    @Test
    fun `getTasksByProjectID returns not deleted tasks when includeDeleted is false`() = runTest {
        coEvery { queryHandler.fetchManyFromCollection(any()) } returns listOf(FAKE_DOCUMENT)
        every { parser.documentToTaskDto(FAKE_DOCUMENT) } returns FAKE_TASK_DTO

        val result = dataSource.getTasksByProjectID(FAKE_PROJECT_ID, false)

        assertThat( result).isEqualTo(listOf(FAKE_TASK))
    }

    @Test
    fun `getTasksByProjectID returns deleted tasks when includeDeleted is true`() = runTest {
        coEvery { queryHandler.fetchManyFromCollection(any()) } returns listOf(FAKE_DOCUMENT)
        every { parser.documentToTaskDto(FAKE_DOCUMENT) } returns FAKE_TASK_DTO

        val result = dataSource.getTasksByProjectID(FAKE_PROJECT_ID, true)

        assertThat( result).isEqualTo(listOf(FAKE_TASK))
    }

    @Test
    fun `getTaskByID returns mapped task`() = runTest {
        coEvery { queryHandler.fetchOneFromCollection(any()) } returns FAKE_DOCUMENT
        every { parser.documentToTaskDto(FAKE_DOCUMENT) } returns FAKE_TASK_DTO

        val result = dataSource.getTaskByID(FAKE_TASK_ID, false)

        assertThat( result).isEqualTo(FAKE_TASK)
    }

    @Test
    fun `getTaskTitleById returns task title`() = runTest {
        coEvery { queryHandler.fetchOneFromCollection(any()) } returns FAKE_DOCUMENT
        every { parser.documentToTaskDto(FAKE_DOCUMENT) } returns FAKE_TASK_DTO

        val result = dataSource.getTaskTitleById(FAKE_TASK_ID)

        assertEquals(FAKE_TASK.title, result)
    }

    @Test
    fun `addNewTask calls insertToCollection`() = runTest {
        every { parser.taskDtoToDocument(FAKE_TASK_DTO) } returns FAKE_DOCUMENT

        dataSource.addNewTask(FAKE_TASK, FAKE_PROJECT_ID)

        coVerify { queryHandler.insertToCollection(FAKE_DOCUMENT) }
    }

    @Test
    fun `editTaskTitle calls updateCollection`() = runTest {
        dataSource.editTaskTitle(FAKE_TASK_ID, "New Title")

        coVerify { queryHandler.updateCollection(TITLE_FIELD, "New Title", any()) }
    }

    @Test
    fun `editTaskDescription calls updateCollection`() = runTest {
        dataSource.editTaskDescription(FAKE_TASK_ID, "New Description")

        coVerify { queryHandler.updateCollection(DESCRIPTION_FIELD, "New Description", any()) }
    }

    @Test
    fun `editTaskState calls updateCollection`() = runTest {
        dataSource.editTaskState(FAKE_TASK_ID, FAKE_STATE_ID)

        coVerify { queryHandler.updateCollection(STATE_ID_FIELD, FAKE_STATE_ID.toString(), any()) }
    }

    @Test
    fun `deleteTask calls softDeleteFromCollection`() = runTest {
        dataSource.deleteTask(FAKE_TASK_ID)

        coVerify { queryHandler.softDeleteFromCollection(any()) }
    }

    private companion object {
        val FAKE_PROJECT_ID: UUID = UUID.fromString("11111111-1111-1111-1111-111111111111")
        val FAKE_TASK_ID: UUID = UUID.fromString("22222222-2222-2222-2222-222222222222")
        val FAKE_STATE_ID: UUID = UUID.fromString("33333333-3333-3333-3333-333333333333")
        val FAKE_TASK = Task(
            id = FAKE_TASK_ID,
            title = "Test Task",
            description = "Test Description",
            taskStateId = FAKE_STATE_ID,
        )
        val FAKE_TASK_DTO = TaskDto(
            id = FAKE_TASK_ID.toString(),
            title = "Test Task",
            description = "Test Description",
            stateId = FAKE_STATE_ID.toString(),
            isDeleted = false,
            projectId = FAKE_PROJECT_ID.toString()
        )
        val FAKE_DOCUMENT: Document = Document(ID_FIELD, FAKE_TASK_DTO.id)
            .append(TITLE_FIELD, FAKE_TASK_DTO.title)
            .append(DESCRIPTION_FIELD, FAKE_TASK_DTO.description)
            .append(STATE_ID_FIELD, FAKE_TASK_DTO.stateId)
            .append(PROJECT_ID_FIELD, FAKE_TASK_DTO.projectId)
            .append(IS_DELETED_FIELD, FAKE_TASK_DTO.isDeleted)
    }
}