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

        assertThat(logs)
    }

    @Test
    fun `getLogsByEntityId returns correct log`() {
        val validLogLine = "123e4567-e89b-12d3-a456-426614174000,987e6543-e21b-32d3-c456-426614174111,2025-04-30T12:00:00,987e6543-e21b-32d3-c456-426614174222,Task,Create"
        every { csvReader.readLines(any()) } returns listOf(validLogLine)

        val logs = logsRepository.getLogsByEntityId(UUID.fromString("987e6543-e21b-32d3-c456-426614174222"))

        assertThat(logs)
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

        verify { csvWriter.appendLine(any(), any()) }
    }

    @Test
    fun `getAllLogs skips invalid log line`() {
        val invalidLogLine = "invalid,log,line,with,fewer,columns"
        every { csvReader.readLines(any()) } returns listOf(invalidLogLine)

        val logs = logsRepository.getAllLogs()

        assertThat(logs).isEmpty()
    }
    @Test
    fun `getAllLogs returns parsed log from valid line`() {
        val validLine = "123e4567-e89b-12d3-a456-426614174000,987e6543-e21b-32d3-c456-426614174111,2025-04-30T12:00:00,987e6543-e21b-32d3-c456-426614174222,Task,Create"
        every { csvReader.readLines(any()) } returns listOf(validLine)

        val logs = logsRepository.getAllLogs()

        assertThat(logs)
    }


    @Test
    fun `getAllLogs skips log when parseLog fails`() {
        val badLine = "bad,line,data,missing,columns"
        every { csvReader.readLines(any()) } returns listOf(badLine)

        val logs = logsRepository.getAllLogs()

        assertThat(logs).isEmpty()
    }
    @Test
    fun `getAllLogs skips log when entity type is unknown`() {
        val line = "id1,id2,2025-04-30T12:00:00,id4,Unknown,Create"
        every { csvReader.readLines(any()) } returns listOf(line)

        val logs = logsRepository.getAllLogs()

        assertThat(logs).isEmpty()
    }
    @Test
    fun `getAllLogs skips log when action type is unknown`() {
        val line = "id1,id2,2025-04-30T12:00:00,id4,Task,UnknownAction"
        every { csvReader.readLines(any()) } returns listOf(line)

        val logs = logsRepository.getAllLogs()

        assertThat(logs).isEmpty()
    }
    @Test
    fun `addLog formats line correctly and calls writer`() {
        val log = Log(
            UUID.randomUUID(),
            User(UUID.randomUUID(), "Ali", User.Type.MATE),
            LocalDateTime.of(2025, 4, 30, 12, 0),
            Create(Task(UUID.randomUUID(), "task", "desc"))
        )
        every { csvWriter.appendLine(any(), any()) } just Runs

        logsRepository.addLog(log)

        verify {
            csvWriter.appendLine(match { it.endsWith(".csv") }, match {
                it.contains("Task") && it.contains("Create")
            })
        }
    }

}
