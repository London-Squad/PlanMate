package data.dataSource

import com.google.common.truth.Truth.assertThat
import data.CsvProjectsDataSource
import io.mockk.every
import io.mockk.mockk
import logic.entities.Project
import logic.entities.State
import logic.entities.Task
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import java.util.UUID

class CsvProjectsDataSourceTest {

    private lateinit var tempFile: File
    private lateinit var repository: CsvProjectsDataSource

    @BeforeEach
    fun setUp() {
        tempFile = File.createTempFile("projects", ".csv")
        repository = CsvProjectsDataSource(tempFile)
    }

    @AfterEach
    fun tearDown() {
        tempFile.delete()
    }

    @Test
    fun `init creates file with correct header when file does not exist`() {
        val tempDir = File(System.getProperty("java.io.tmpdir"), "testDir_${UUID.randomUUID()}")
        val newFile = File(tempDir, "projects.csv")
        val newRepository = CsvProjectsDataSource(newFile)
        assertThat(newFile.readText()).isEqualTo("id,title,description,tasks,states\n")
    }

    @Test
    fun `init preserves existing file content`() {
        tempFile.writeText("id,title,description,tasks,states\n123e4567-e89b-12d3-a456-426614174000,Old Title,Old Description,,${State.NoState.id}|No State|Default state\n")
        val newRepository = CsvProjectsDataSource(tempFile)
        assertThat(tempFile.readText()).isEqualTo("id,title,description,tasks,states\n123e4567-e89b-12d3-a456-426614174000,Old Title,Old Description,,${State.NoState.id}|No State|Default state\n")
    }

    @Test
    fun `getAllProjects returns empty list for empty file`() {
        tempFile.writeText("id,title,description,tasks,states\n")
        val projects = repository.getAllProjects()
        assertThat(projects).isEmpty()
    }

    @Test
    fun `getAllProjects parses project with tasks and states`() {
        val projectId = UUID.randomUUID()
        val taskId = UUID.randomUUID()
        val stateId = UUID.randomUUID()
        tempFile.writeText(
            "id,title,description,tasks,states\n" +
                    "$projectId,Test Project,Test Description," +
                    "$taskId|Task Title|Task Desc|$stateId," +
                    "$stateId|Test State|State Desc"
        )
        val projects = repository.getAllProjects()
        assertThat(projects).containsExactly(
            Project(
                id = projectId,
                title = "Test Project",
                description = "Test Description",
                tasks = listOf(
                    Task(
                        id = taskId,
                        title = "Task Title",
                        description = "Task Desc",
                        state = State(stateId, "No State", "Default state")
                    )
                ),
                states = listOf(State(stateId, "Test State", "State Desc"))
            )
        )
    }

    @Test
    fun `addNewProject appends project with tasks and states`() {
        val projectId = UUID.randomUUID()
        val taskId = UUID.randomUUID()
        val stateId = UUID.randomUUID()
        val project = mockk<Project> {
            every { id } returns projectId
            every { title } returns "Project 1"
            every { description } returns "Description 1"
            every { tasks } returns listOf(
                Task(taskId, "Task 1", "Task Desc 1", State(stateId, "TODO", "To Do Tasks"))
            )
            every { states } returns listOf(State(stateId, "TODO", "To Do Tasks"))
        }
        repository.addNewProject(project)
        assertThat(tempFile.readText()).isEqualTo(
                    "$projectId,Project 1,Description 1," +
                    "$taskId|Task 1|Task Desc 1|$stateId," +
                    "$stateId|TODO|To Do Tasks\n"
        )
    }

    @Test
    fun `editProjectTitle updates title correctly`() {
        val projectId = UUID.randomUUID()
        tempFile.writeText("id,title,description,tasks,states\n$projectId,Old Title,Old Desc,,\n")
        repository.editProjectTitle(projectId, "New Title")
        assertThat(tempFile.readText()).isEqualTo("id,title,description,tasks,states\n$projectId,New Title,Old Desc,,\n")
    }

    @Test
    fun `editProjectDescription updates description correctly`() {
        val projectId = UUID.randomUUID()
        tempFile.writeText("id,title,description,tasks,states\n$projectId,Old Title,Old Desc,,\n")
        repository.editProjectDescription(projectId, "New Desc")
        assertThat(tempFile.readText()).isEqualTo("id,title,description,tasks,states\n$projectId,Old Title,New Desc,,\n")
    }

    @Test
    fun `deleteProject removes specified project`() {
        val projectId1 = UUID.randomUUID()
        val projectId2 = UUID.randomUUID()
        tempFile.writeText(
            "id,title,description,tasks,states\n" +
                    "$projectId1,Project 1,Desc 1,,\n" +
                    "$projectId2,Project 2,Desc 2,,\n"
        )
        repository.deleteProject(projectId1)
        assertThat(tempFile.readText()).isEqualTo("id,title,description,tasks,states\n$projectId2,Project 2,Desc 2,,\n")
    }
}