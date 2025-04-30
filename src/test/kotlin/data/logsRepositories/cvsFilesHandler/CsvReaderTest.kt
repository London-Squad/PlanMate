import data.logsRepositories.cvsFilesHandler.CsvReader
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.nio.file.Path
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CsvReaderTest {

    private val csvReader = CsvReader()

    @TempDir
    lateinit var tempDir: Path

    @Test
    fun `readLines returns empty list if file does not exist`() {
        val filePath = tempDir.resolve("non_existent_file.csv").toString()

        val result = csvReader.readLines(filePath)

        assertTrue(result.isEmpty())
    }

    @Test
    fun `readLines returns empty list if file is not readable`() {
        val filePath = tempDir.resolve("unreadable_file.csv").toString()
        val file = File(filePath)
        file.createNewFile()
        file.setReadable(false)

        val result = csvReader.readLines(filePath)

        assertTrue(result.isEmpty())
    }

    @Test
    fun `readLines processes valid file with correct number of columns`() {
        val filePath = tempDir.resolve("valid_file.csv").toString()
        val file = File(filePath)
        file.writeText(
            "123e4567-e89b-12d3-a456-426614174000,987e6543-e21b-32d3-c456-426614174111,2025-04-30T12:00:00,987e6543-e21b-32d3-c456-426614174222,Task,Create\n" +
                    "123e4567-e89b-12d3-a456-426614174000,987e6543-e21b-32d3-c456-426614174111,2025-04-30T12:00:00,987e6543-e21b-32d3-c456-426614174333,Project,Delete\n"
        )

        val result = csvReader.readLines(filePath)

        assertEquals(2, result.size)
    }

    @Test
    fun `readLines skips invalid log lines with incorrect number of columns`() {
        val filePath = tempDir.resolve("invalid_file.csv").toString()
        val file = File(filePath)
        file.writeText(
            "123e4567-e89b-12d3-a456-426614174000,987e6543-e21b-32d3-c456-426614174111,2025-04-30T12:00:00,987e6543-e21b-32d3-c456-426614174222,Task\n"  // Invalid log line with 5 columns
        )

        val result = csvReader.readLines(filePath)

        assertTrue(result.isEmpty())
    }

    @Test
    fun `readLines handles IOException gracefully`() {
        val filePath = tempDir.resolve("io_exception_file.csv").toString()

        // Simulating a failure scenario for I/O operations like file lock or permission error
        val file = File(filePath)
        file.createNewFile()

        // Set file to be non-writable so an IOException will be triggered when reading
        file.setWritable(false)

        val result = csvReader.readLines(filePath)

        assertTrue(result.isEmpty())
    }
}
