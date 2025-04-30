import com.google.common.truth.Truth.assertThat
import data.logsRepositories.csvFilesHandler.LogsRepositoryImpl
import data.logsRepositories.cvsFilesHandler.CsvReader
import data.logsRepositories.cvsFilesHandler.CsvWriter
import logic.entities.*

import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.UUID


class LogsRepositoryImplTest {

    private lateinit var csvReader: CsvReader
    private lateinit var csvWriter: CsvWriter
    private lateinit var logsRepository: LogsRepositoryImpl

    @BeforeEach
    fun setUp() {
        csvReader = mockk()
        csvWriter = mockk()
        logsRepository = LogsRepositoryImpl(csvReader, csvWriter)
    }


    @Test
    fun `getAllLogs parses valid logs`() {
        val validLogLine = "123e4567-e89b-12d3-a456-426614174000,987e6543-e21b-32d3-c456-426614174111,2025-04-30T12:00:00,987e6543-e21b-32d3-c456-426614174222,Task,Create"
        every { csvReader.readLines(any()) } returns listOf(validLogLine)

        val logs = logsRepository.getAllLogs()

        assertThat(logs).hasSize(1)  // Single assertion
    }

    @Test
    fun `getLogById returns correct log`() {
        val validLogLine = "123e4567-e89b-12d3-a456-426614174000,987e6543-e21b-32d3-c456-426614174111,2025-04-30T12:00:00,987e6543-e21b-32d3-c456-426614174222,Task,Create"
        every { csvReader.readLines(any()) } returns listOf(validLogLine)

        val logs = logsRepository.getLogById(UUID.fromString("987e6543-e21b-32d3-c456-426614174222"))

        assertThat(logs).hasSize(1)  // Single assertion
    }

    @Test
    fun `addLog writes a log to CSV`() {
        val log = Log(
            UUID.randomUUID(),
            User(UUID.randomUUID(), "User", User.Type.MATE),
            LocalDateTime.now(),
            Create(Task(UUID.randomUUID(), "Task Name", "Description"))
        )

        every { csvWriter.appendLine(any(), any()) } just Runs

        logsRepository.addLog(log)

        verify { csvWriter.appendLine(any(), any()) }  // Single assertion
    }

    @Test
    fun `getAllLogs skips invalid log line`() {
        val invalidLogLine = "invalid,log,line,with,fewer,columns"
        every { csvReader.readLines(any()) } returns listOf(invalidLogLine)

        val logs = logsRepository.getAllLogs()

        assertThat(logs).isEmpty()  // Single assertion
    }

    @Test
    fun `parseLog handles exceptions`() {
        val invalidLine = "invalid,log,line,with,exception"
        val result = logsRepository.parseLog(invalidLine)

        assertThat(result).isNull()  // Single assertion
    }

    @Test
    fun `getEntityByType returns correct entity`() {
        val taskEntity = logsRepository.getEntityByType("Task", UUID.randomUUID())

        assertThat(taskEntity).isInstanceOf(Task::class.java)  // Single assertion
    }

    @Test
    fun `getActionByType returns correct action`() {
        val task = Task(UUID.randomUUID(), "Task", "Description")

        val createAction = logsRepository.getActionByType("Create", task)

        assertThat(createAction).isInstanceOf(Create::class.java)  // Single assertion
    }
}
