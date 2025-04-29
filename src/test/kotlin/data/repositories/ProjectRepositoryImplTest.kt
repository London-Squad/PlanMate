package data.repositories
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


class ProjectRepositoryImplTest {

    private lateinit var projectRepository: ProjectRepositoryImpl


    @BeforeEach
    fun setUp() {
        projectRepository = ProjectRepositoryImpl()
    }
    @Test
    fun `getAllProjects should return empty list when no projects exist`() {
    }

    @Test
    fun `getAllProjects should return one project when on csv just one project`() {
    }

    @Test
    fun `getAllProjects should return all projects`() {
    }


    @Test
    fun `getAllProjects should throw exception when something happen while reading data file`() {
    }


}