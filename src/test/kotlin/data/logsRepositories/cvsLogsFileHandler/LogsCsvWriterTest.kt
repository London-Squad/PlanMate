//import com.google.common.truth.Truth.assertThat
//import data.logsRepositories.cvsFilesHandler.LogsCsvWriter
//import data.logsRepositories.cvsFilesHandler.FilePaths
//import org.junit.jupiter.api.Test
//import java.io.File
//
//
//class LogsCsvWriterTest {
//
//    private val logsCsvWriter = LogsCsvWriter()
//
//    @Test
//    fun `create directory if it doesn't exist`() {
//        // Use the file path from FilePaths
//        val filePath = FilePaths.allLogsFile
//        val file = File(filePath)
//
//        // Ensure the directory exists before writing
//        file.parentFile?.mkdirs()
//
//        // Assert that the directory exists (one assertion)
//        assertThat(file.parentFile?.exists()).isTrue()
//
//        // Cleanup after test
//        file.delete()
//    }
//
//    @Test
//    fun `append line to the file`() {
//        // Use the file path from FilePaths
//        val filePath = FilePaths.allLogsFile
//        val file = File(filePath)
//
//        // Ensure the directory exists before writing
//        file.parentFile?.mkdirs()
//
//        // Append a line
//        logsCsvWriter.appendLine(filePath, "123e4567-e89b-12d3-a456-426614174000,987e6543-e21b-32d3-c456-426614174111,2025-04-30T12:00:00,987e6543-e21b-32d3-c456-426614174222,Task,Create")
//
//        // Assert that the file contains the appended line (one assertion)
//        assertThat(file.readText()).contains("123e4567-e89b-12d3-a456-426614174000")
//
//        // Cleanup after test
//        file.delete()
//    }
//
//
//
//}
