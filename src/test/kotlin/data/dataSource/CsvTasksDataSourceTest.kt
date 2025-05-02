package data.dataSource

import com.google.common.truth.Truth.assertThat
import data.CsvTasksDataSource
import io.mockk.every
import io.mockk.mockk
import logic.entities.State
import logic.entities.Task
import logic.repositories.StatesRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import java.util.*

class CsvTasksDataSourceTest {

    private lateinit var tempFile: File
    private lateinit var stateRepository: StatesRepository
    private lateinit var csvTasksDataSource: CsvTasksDataSource

    @BeforeEach
    fun setUp() {
        stateRepository = mockk(relaxed = true)
        tempFile = File.createTempFile("tasksTest", ".csv")

        csvTasksDataSource = CsvTasksDataSource(tempFile, stateRepository)
    }

    @AfterEach
    fun tearDown() {
        tempFile.delete()
    }

    @Test
    fun `init should create directory and file when they do not exist`() {
        val tempDir = File(System.getProperty("java.io.tmpdir"), "testDir_${UUID.randomUUID()}")
        val newFile = File(tempDir, "tasks.csv")

        CsvTasksDataSource(newFile, stateRepository)

        assertThat(newFile.readText()).isEqualTo("id,title,description,stateID,projectID,\n")
    }

    @Test
    fun `init should not overwrite file when it exists`() {
        tempFile.writeText(
            "id,title,description,stateID,projectID,\n" +
                    "873354d1-28f9-4a2d-ba80-e5fbe2600220,New Task,Default description,07fa94e1-8030-41ad-8342-fefa210461ce,06eb39bb-3131-47e5-a4cd-54bce025e935\n"
        )

        CsvTasksDataSource(tempFile, stateRepository)

        assertThat(tempFile.readText()).isEqualTo(
            "id,title,description,stateID,projectID,\n" +
                    "873354d1-28f9-4a2d-ba80-e5fbe2600220,New Task,Default description,07fa94e1-8030-41ad-8342-fefa210461ce,06eb39bb-3131-47e5-a4cd-54bce025e935\n"
        )
    }

    @Test
    fun `getAllTasksByProjectID should return empty task list when there are no tasks in the file with the specified projectID`() {
        val wantedProjectID = UUID.randomUUID()
        val anotherProjectID = UUID.randomUUID()
        tempFile.writeText(
            "id,title,description,stateID,projectID,\n" +
                    "873354d1-28f9-4a2d-ba80-e5fbe2600220,New Task,Default description,07fa94e1-8030-41ad-8342-fefa210461ce,$anotherProjectID\n"
        )

        val tasks = csvTasksDataSource.getAllTasksByProjectID(wantedProjectID)

        assertThat(tasks).isEmpty()
    }

    @Test
    fun `getAllTasksByProjectID should return task list when there are tasks in the file with the specified projectID`() {
        every { stateRepository.getStateById(any()) } returns State.NoState
        val wantedProjectID = UUID.randomUUID()
        val anotherProjectID = UUID.randomUUID()
        tempFile.writeText(
            "id,title,description,stateID,projectID,\n" +
                    "873354d1-28f9-4a2d-ba80-e5fbe2600220,first Task,first description,07fa94e1-8030-41ad-8342-fefa210461ce,$anotherProjectID\n" +
                    "${dummyTasks[0].id},${dummyTasks[0].title},${dummyTasks[0].description},${dummyTasks[0].state.id},$wantedProjectID\n" +
                    "${dummyTasks[1].id},${dummyTasks[1].title},${dummyTasks[1].description},${dummyTasks[1].state.id},$wantedProjectID\n"
        )

        val tasks = csvTasksDataSource.getAllTasksByProjectID(wantedProjectID)

        assertThat(tasks).containsExactly(dummyTasks[0], dummyTasks[1])
    }

    @Test
    fun `addNewProject appends first project to file`() {
        val projectID = UUID.randomUUID()
        val task = dummyTasks[0]

        csvTasksDataSource.addNewTask(task, projectID)

        assertThat(tempFile.readText()).isEqualTo(
            "${task.id},${task.title},${task.description},${task.state.id},${projectID}\n"
        )
    }

    @Test
    fun `addNewProject appends second project to file with existing data`() {
        val projectID = UUID.randomUUID()
        val task1 = dummyTasks[0]
        val task2 = dummyTasks[1]

        csvTasksDataSource.addNewTask(task1, projectID)
        csvTasksDataSource.addNewTask(task2, projectID)

        assertThat(tempFile.readText()).isEqualTo(
            "${task1.id},${task1.title},${task1.description},${task1.state.id},${projectID}\n" +
                    "${task2.id},${task2.title},${task2.description},${task2.state.id},${projectID}\n"
        )
    }

    private companion object {
        val dummyTasks = listOf(
            Task(
                id = UUID.fromString("873354d2-28f9-4a2d-ba80-e5fbe2600220"),
                title = "second Task",
                description = "second description",
                state = State.NoState
            ),
            Task(
                id = UUID.fromString("873354d3-28f9-4a2d-ba80-e5fbe2600220"),
                title = "third Task",
                description = "third description",
                state = State.NoState
            )
        )
    }

    @Test
    fun `editTaskTitle updates title of existing task`() {
        every { stateRepository.getStateById(any()) } returns State.NoState
        val taskId = UUID.randomUUID()
        tempFile.writeText(
            "id,title,description,stateID,projectID,\n" +
                    "${taskId},Old Task,Default description,07fa94e1-8030-41ad-8342-fefa210461ce,06eb39bb-3131-47e5-a4cd-54bce025e935\n"
        )
        val newTitle = "Updated Title"

        csvTasksDataSource.editTaskTitle(taskId, newTitle)

        assertThat(tempFile.readText()).isEqualTo(
            "id,title,description,stateID,projectID,\n" +
                    "${taskId},$newTitle,Default description,07fa94e1-8030-41ad-8342-fefa210461ce,06eb39bb-3131-47e5-a4cd-54bce025e935\n"
        )
    }

    @Test
    fun `editTaskDescription updates description of existing task`() {
        every { stateRepository.getStateById(any()) } returns State.NoState
        val taskId = UUID.randomUUID()
        tempFile.writeText(
            "id,title,description,stateID,projectID,\n" +
                    "${taskId},Test Task,old description,07fa94e1-8030-41ad-8342-fefa210461ce,06eb39bb-3131-47e5-a4cd-54bce025e935\n"
        )
        val newDescription = "Updated Description"

        csvTasksDataSource.editTaskDescription(taskId, newDescription)

        assertThat(tempFile.readText()).isEqualTo(
            "id,title,description,stateID,projectID,\n" +
                    "${taskId},Test Task,$newDescription,07fa94e1-8030-41ad-8342-fefa210461ce,06eb39bb-3131-47e5-a4cd-54bce025e935\n"
        )
    }

    @Test
    fun `editTaskState updates description of existing task`() {

        val firstTaskOldState = State(UUID.randomUUID(), "oldState", "for test purpose")
        val firstTaskNewState = State(UUID.randomUUID(), "newState", "for test purpose")
        val secondTaskState = State(UUID.randomUUID(), "newState", "for test purpose")
        every { stateRepository.getStateById(any()) } answers { firstTaskOldState } andThenAnswer { secondTaskState } andThenAnswer {firstTaskNewState}
        val taskId = UUID.randomUUID()
        val anotherTaskId = UUID.randomUUID()
        tempFile.writeText(
            "id,title,description,stateID,projectID,\n" +
                    "${taskId},Test Task,description,${firstTaskOldState.id},06eb39bb-3131-47e5-a4cd-54bce025e935\n" +
                    "${anotherTaskId},Test Task,description,${secondTaskState.id},06eb39bb-3131-47e5-a4cd-54bce025e935\n"
        )

        csvTasksDataSource.editTaskState(taskId, firstTaskNewState)

        assertThat(tempFile.readText()).isEqualTo(
            "id,title,description,stateID,projectID,\n" +
                    "${taskId},Test Task,description,${firstTaskNewState.id},06eb39bb-3131-47e5-a4cd-54bce025e935\n" +
                    "${anotherTaskId},Test Task,description,${secondTaskState.id},06eb39bb-3131-47e5-a4cd-54bce025e935\n"
        )
    }

    @Test
    fun `deleteTask should delete the task when it exists`() {
        every { stateRepository.getStateById(any()) } returns State.NoState
        val projectID = UUID.randomUUID()
        val task1 = dummyTasks[0]
        val task2 = dummyTasks[1]

        tempFile.writeText(
            "${task1.id},${task1.title},${task1.description},${task1.state.id},${projectID}\n" +
                    "${task2.id},${task2.title},${task2.description},${task2.state.id},${projectID}\n"
        )

        csvTasksDataSource.deleteTask(task1.id)

        assertThat(tempFile.readText()).isEqualTo(
            "id,title,description,stateID,projectID,\n" +
                    "${task2.id},${task2.title},${task2.description},${task2.state.id},${projectID}\n"
        )
    }
}