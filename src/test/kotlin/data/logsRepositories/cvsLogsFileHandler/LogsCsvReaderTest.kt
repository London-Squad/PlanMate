//import data.fileIO.createFileIfNotExist
//import data.logsRepositories.cvsFilesHandler.LogsCsvReader
//import io.mockk.mockk
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.Test
//import java.io.File
//import kotlin.test.AfterTest
//import kotlin.test.BeforeTest
//import kotlin.test.assertEquals
//import kotlin.test.assertTrue
//
//class LogsCsvReaderTest {
//
//    private lateinit var logsFile: File
//
//    private lateinit var logsCsvReader: LogsCsvReader
//
//    @BeforeTest
//    fun preSetup() {
//        logsFile = File("logsFileTest.csv")
//        logsFile.createFileIfNotExist( "id,userId,actionType,entityId,entityType,time,property,oldValue,newValue\n")
//    }
//
//    @AfterTest
//    fun tearDown() {
//        logsFile.delete()
//    }
//
//    @BeforeEach
//    fun setUp() {
//        logsCsvReader = LogsCsvReader(
//            logsFile
//        )
//    }
//
//    @Test
//    fun `readLines processes valid file with correct number of columns`() {
//        val file = File(filePath)
//        logsFile.writeText(
//            "123e4567-e89b-12d3-a456-426614174000,987e6543-e21b-32d3-c456-426614174111,2025-04-30T12:00:00,987e6543-e21b-32d3-c456-426614174222,Task,Create\n" +
//                    "123e4567-e89b-12d3-a456-426614174000,987e6543-e21b-32d3-c456-426614174111,2025-04-30T12:00:00,987e6543-e21b-32d3-c456-426614174333,Project,Delete\n"
//        )
//
//        val result = logsCsvReader.readRows(filePath)
//
//        assertEquals(2, result.size)
//    }
//
//    @Test
//    fun `readLines skips invalid log lines with incorrect number of columns`() {
//        val filePath = tempDir.resolve("invalid_file.csv").toString()
//        val file = File(filePath)
//        file.writeText(
//            "123e4567-e89b-12d3-a456-426614174000,987e6543-e21b-32d3-c456-426614174111,2025-04-30T12:00:00,987e6543-e21b-32d3-c456-426614174222,Task\n"  // Invalid log line with 5 columns
//        )
//
//        val result = logsCsvReader.readRows(filePath)
//
//        assertTrue(result.isEmpty())
//    }
//
//    @Test
//    fun `readLines handles IOException gracefully`() {
//        val filePath = tempDir.resolve("io_exception_file.csv").toString()
//
//        // Simulating a failure scenario for I/O operations like file lock or permission error
//        val file = File(filePath)
//        file.createNewFile()
//
//        // Set file to be non-writable so an IOException will be triggered when reading
//        file.setWritable(false)
//
//        val result = logsCsvReader.readRows(filePath)
//
//        assertTrue(result.isEmpty())
//    }
//}
