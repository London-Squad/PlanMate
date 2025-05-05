package data.parser

import data.csvHandler.CsvFileHandler
import logic.entities.Project
import logic.repositories.StatesRepository
import logic.repositories.TaskRepository
import java.util.UUID

class ProjectParser(
    private val taskRepository: TaskRepository,
    private val statesRepository: StatesRepository,
    private val csvHandler: CsvFileHandler
) {
    fun parseProjectLine(line: String): ProjectParseResult {
        val parts = csvHandler.decodeRecord(line)
        if (parts.size < 3) return ProjectParseResult.Failure("Invalid project line format: $line")
        return try {
            val projectId = UUID.fromString(parts[0].trim())
            ProjectParseResult.Success(
                Project(
                    id = projectId,
                    title = parts[1].trim(),
                    description = parts[2].trim(),
                    tasks = taskRepository.getAllTasksByProjectID(projectId),
                    states = statesRepository.getAllStatesByProjectId(projectId)
                )
            )
        } catch (e: IllegalArgumentException) {
            ProjectParseResult.Failure("Failed to parse project line: $line, reason: ${e.message}")
        }
    }

    fun formatProjectLine(project: Project): String {
        val record = listOf(
            project.id.toString(),
            project.title,
            project.description
        )
        return csvHandler.encodeRecord(record)
    }
}

sealed class ProjectParseResult {
    data class Success(val project: Project) : ProjectParseResult()
    data class Failure(val reason: String) : ProjectParseResult()
}