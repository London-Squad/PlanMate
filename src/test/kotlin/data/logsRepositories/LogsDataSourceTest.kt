//import com.google.common.truth.Truth.assertThat
//import data.logsRepositories.csvFilesHandler.LogsDataSource
//import data.logsRepositories.cvsFilesHandler.LogsCsvReader
//import data.logsRepositories.cvsFilesHandler.LogsCsvWriter
//import logic.entities.*
//
//import io.mockk.*
//import logic.repositories.ProjectsRepository
//import logic.repositories.StatesRepository
//import logic.repositories.TaskRepository
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.Test
//import java.time.LocalDateTime
//import java.util.UUID
//
//
//class LogsDataSourceTest {
//
//    private lateinit var logsCsvReader: LogsCsvReader
//    private lateinit var logsCsvWriter: LogsCsvWriter
//    private lateinit var logsRepository: LogsDataSource
//    private lateinit var projectsRepository: ProjectsRepository
//    private lateinit var statesRepository: StatesRepository
//    private lateinit var tasksRepository: TaskRepository
//
//    @BeforeEach
//    fun setUp() {
//        logsCsvReader = mockk()
//        logsCsvWriter = mockk()
//        projectsRepository = mockk()
//        statesRepository = mockk()
//        tasksRepository = mockk()
//
//        logsRepository = LogsDataSource(
//            logsCsvReader,
//            logsCsvWriter,
//            projectsRepository,
//            statesRepository,
//            tasksRepository,
//        )
//    }
//
//
//    @Test
//    fun `getAllLogs parses valid logs`() {
//        val validLogLine = "123e4567-e89b-12d3-a456-426614174000,987e6543-e21b-32d3-c456-426614174111,2025-04-30T12:00:00,987e6543-e21b-32d3-c456-426614174222,Task,Create"
//        every { logsCsvReader.readRows() } returns listOf(validLogLine)
//
//        val logs = logsRepository.getAllLogs()
//
//        assertThat(logs)
//    }
//
//    @Test
//    fun `getLogsByEntityId returns correct log`() {
//        val validLogLine = "123e4567-e89b-12d3-a456-426614174000,987e6543-e21b-32d3-c456-426614174111,2025-04-30T12:00:00,987e6543-e21b-32d3-c456-426614174222,Task,Create"
//        every { logsCsvReader.readRows() } returns listOf(validLogLine)
//
//        val logs = logsRepository.getLogsByEntityId(UUID.fromString("987e6543-e21b-32d3-c456-426614174222"))
//
//        assertThat(logs)
//    }
//
//    @Test
//    fun `addLog writes a log to CSV`() {
//        val log = Log(
//            UUID.randomUUID(),
//            User(UUID.randomUUID(), "User", User.Type.MATE),
//            LocalDateTime.now(),
//            Create(Task(UUID.randomUUID(), "Task Name", "Description"))
//        )
//
//        every { logsCsvWriter.appendLine(any()) } just Runs
//
//        logsRepository.addLog(log)
//
//        verify { logsCsvWriter.appendLine(any()) }
//    }
//
//    @Test
//    fun `getAllLogs skips invalid log line`() {
//        val invalidLogLine = "invalid,log,line,with,fewer,columns"
//        every { logsCsvReader.readRows() } returns listOf(invalidLogLine)
//
//        val logs = logsRepository.getAllLogs()
//
//        assertThat(logs).isEmpty()
//    }
//    @Test
//    fun `getAllLogs returns parsed log from valid line`() {
//        val validLine = "123e4567-e89b-12d3-a456-426614174000,987e6543-e21b-32d3-c456-426614174111,2025-04-30T12:00:00,987e6543-e21b-32d3-c456-426614174222,Task,Create"
//        every { logsCsvReader.readRows(any()) } returns listOf(validLine)
//
//        val logs = logsRepository.getAllLogs()
//
//        assertThat(logs)
//    }
//
//
//    @Test
//    fun `getAllLogs skips log when parseLog fails`() {
//        val badLine = "bad,line,data,missing,columns"
//        every { logsCsvReader.readRows(any()) } returns listOf(badLine)
//
//        val logs = logsRepository.getAllLogs()
//
//        assertThat(logs).isEmpty()
//    }
//    @Test
//    fun `getAllLogs skips log when entity type is unknown`() {
//        val line = "id1,id2,2025-04-30T12:00:00,id4,Unknown,Create"
//        every { logsCsvReader.readRows(any()) } returns listOf(line)
//
//        val logs = logsRepository.getAllLogs()
//
//        assertThat(logs).isEmpty()
//    }
//    @Test
//    fun `getAllLogs skips log when action type is unknown`() {
//        val line = "id1,id2,2025-04-30T12:00:00,id4,Task,UnknownAction"
//        every { logsCsvReader.readRows(any()) } returns listOf(line)
//
//        val logs = logsRepository.getAllLogs()
//
//        assertThat(logs).isEmpty()
//    }
//    @Test
//    fun `addLog formats line correctly and calls writer`() {
//        val log = Log(
//            UUID.randomUUID(),
//            User(UUID.randomUUID(), "Ali", User.Type.MATE),
//            LocalDateTime.of(2025, 4, 30, 12, 0),
//            Create(Task(UUID.randomUUID(), "task", "desc"))
//        )
//        every { logsCsvWriter.appendLine(any(), any()) } just Runs
//
//        logsRepository.addLog(log)
//
//        verify {
//            logsCsvWriter.appendLine(match { it.endsWith(".csv") }, match {
//                it.contains("Task") && it.contains("Create")
//            })
//        }
//    }
//
//}
