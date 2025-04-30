package data.repositoriesImpl

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import logic.entities.Project
import logic.entities.State
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import java.util.UUID

class CsvProjectsRepositoryTest {

    private lateinit var tempFile: File
    private lateinit var repository: CsvProjectsRepository

    @BeforeEach
    fun setUp() {
        tempFile = File.createTempFile("projects", ".csv")
        repository = CsvProjectsRepository(tempFile)
    }

    @AfterEach
    fun tearDown() {
        tempFile.delete()
    }

    @Test
    fun `init creates directory and file when they do not exist`() {
        // Given
        val tempDir = File(System.getProperty("java.io.tmpdir"), "testDir_${UUID.randomUUID()}")
        val newFile = File(tempDir, "projects.csv")

        // When
        val newRepository = CsvProjectsRepository(newFile)

        // Then
        assertThat(newFile.readText()).isEqualTo("id,title,description\n")
    }

    @Test
    fun `init does not overwrite file when it exists`() {
        // Given
        tempFile.writeText("id,title,description\n123e4567-e89b-12d3-a456-426614174000,Old Title,Old Description\n")

        // When
        val newRepository = CsvProjectsRepository(tempFile)

        // Then
        assertThat(tempFile.readText()).isEqualTo("id,title,description\n123e4567-e89b-12d3-a456-426614174000,Old Title,Old Description\n")
    }

    @Test
    fun `getAllProjects returns empty list when file is empty`() {
        // Given
        tempFile.writeText("id,title,description\n")

        // When
        val projects = repository.getAllProjects()

        // Then
        assertThat(projects).isEmpty()
    }

    @Test
    fun `getAllProjects returns projects with NoState when file has data`() {
        // Given
        val projectId = UUID.randomUUID()
        tempFile.writeText("id,title,description\n$projectId,Test Project,Test Description\n")

        // When
        val projects = repository.getAllProjects()

        // Then
        assertThat(projects).containsExactly(
            Project(
                id = projectId,
                title = "Test Project",
                description = "Test Description",
                tasks = emptyList(),
                states = listOf(State.NoState)
            )
        )
    }

    @Test
    fun `addNewProject appends first project to file`() {
        // Given
        val project = mockk<Project> {
            every { id } returns UUID.fromString("1c7e901a-0913-4049-b1d1-024258b9789f")
            every { title } returns "Project 1"
            every { description } returns "Description 1"
            every { tasks } returns emptyList()
            every { states } returns listOf(State.NoState)
        }

        // When
        repository.addNewProject(project)

        // Then
        assertThat(tempFile.readText()).isEqualTo("${project.id},Project 1,Description 1\n")
    }

    @Test
    fun `addNewProject appends second project to file with existing data`() {
        // Given
        val project1 = mockk<Project> {
            every { id } returns UUID.fromString("1c7e901a-0913-4049-b1d1-024258b9789f")
            every { title } returns "Project 1"
            every { description } returns "Description 1"
            every { tasks } returns emptyList()
            every { states } returns listOf(State.NoState)
        }
        repository.addNewProject(project1)
        val project2 = mockk<Project> {
            every { id } returns UUID.fromString("2f2678f8-1fdf-40c6-894b-258a7360466b")
            every { title } returns "Project 2"
            every { description } returns "Description 2"
            every { tasks } returns emptyList()
            every { states } returns listOf(State.NoState)
        }

        // When
        repository.addNewProject(project2)

        // Then
        assertThat(tempFile.readText()).isEqualTo(
            "${project1.id},Project 1,Description 1\n${project2.id},Project 2,Description 2\n"
        )
    }

      @Test
    fun `editProjectTitle updates title of existing project`() {
        // Given
        val projectId = UUID.randomUUID()
        tempFile.writeText("id,title,description\n$projectId,Old Title,Old Description\n")
        val newTitle = "Updated Title"

        // When
        repository.editProjectTitle(projectId, newTitle)

        // Then
        assertThat(tempFile.readText()).isEqualTo("id,title,description\n$projectId,Updated Title,Old Description\n")
    }

    @Test
    fun `editProjectTitle does nothing when project does not exist`() {
        // Given
        val projectId = UUID.randomUUID()
        val nonExistentId = UUID.randomUUID()
        tempFile.writeText("id,title,description\n$projectId,Old Title,Old Description\n")
        val newTitle = "Updated Title"

        // When
        repository.editProjectTitle(nonExistentId, newTitle)

        // Then
        assertThat(tempFile.readText()).isEqualTo("id,title,description\n$projectId,Old Title,Old Description\n")
    }

    @Test
    fun `editProjectDescription updates description of existing project`() {
        // Given
        val projectId = UUID.randomUUID()
        tempFile.writeText("id,title,description\n$projectId,Old Title,Old Description\n")
        val newDescription = "Updated Description"

        // When
        repository.editProjectDescription(projectId, newDescription)

        // Then
        assertThat(tempFile.readText()).isEqualTo("id,title,description\n$projectId,Old Title,Updated Description\n")
    }

    @Test
    fun `editProjectDescription does nothing when project does not exist`() {
        // Given
        val projectId = UUID.randomUUID()
        val nonExistentId = UUID.randomUUID()
        tempFile.writeText("id,title,description\n$projectId,Old Title,Old Description\n")
        val newDescription = "Updated Description"

        // When
        repository.editProjectDescription(nonExistentId, newDescription)

        // Then
        assertThat(tempFile.readText()).isEqualTo("id,title,description\n$projectId,Old Title,Old Description\n")
    }

    @Test
    fun `deleteProject removes project from file`() {
        // Given
        val projectId1 = UUID.randomUUID()
        val projectId2 = UUID.randomUUID()
        tempFile.writeText("id,title,description\n$projectId1,Project 1,Desc 1\n$projectId2,Project 2,Desc 2\n")

        // When
        repository.deleteProject(projectId1)

        // Then
        assertThat(tempFile.readText()).isEqualTo("id,title,description\n$projectId2,Project 2,Desc 2\n")
    }

    @Test
    fun `deleteProject does nothing when project does not exist`() {
        // Given
        val projectId = UUID.randomUUID()
        val nonExistentId = UUID.randomUUID()
        tempFile.writeText("id,title,description\n$projectId,Project 1,Desc 1\n")

        // When
        repository.deleteProject(nonExistentId)

        // Then
        assertThat(tempFile.readText()).isEqualTo("id,title,description\n$projectId,Project 1,Desc 1\n")
    }
}