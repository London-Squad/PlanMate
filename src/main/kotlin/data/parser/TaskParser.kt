package data.parser

import data.csvHandler.CsvFileHandler
import logic.entities.Task
import logic.repositories.StatesRepository
import java.util.UUID

class TaskParser(private val statesRepository: StatesRepository) {
    fun parseTaskLine(line: String): TaskParseResult {
        return try {
            val parts = CsvFileHandler.decodeRow(line)
            if (parts.size < 5) return TaskParseResult.Failure("Invalid task line format: $line")

            val id = parts[0]
            val title = parts[1]
            val description = parts[2]
            val stateId = parts[3]
            val projectId = parts[4]

            val taskId = UUID.fromString(id)
            val projectUUID = UUID.fromString(projectId)
            val stateUUID = UUID.fromString(stateId)
            val state = statesRepository.getStateById(stateUUID)

            TaskParseResult.Success(
                projectUUID,
                Task(
                    id = taskId,
                    title = title,
                    description = description,
                    state = state
                )
            )
        } catch (e: Exception) {
            TaskParseResult.Failure("Failed to parse task line: $line, reason: ${e.message}")
        }
    }

    fun formatTaskLine(projectId: UUID, task: Task): String {
        val record = listOf(
            task.id.toString(),
            task.title,
            task.description,
            task.state.id.toString(),
            projectId.toString()
        )
        return CsvFileHandler.encodeRow(record)
    }
}

sealed class TaskParseResult {
    data class Success(val projectId: UUID, val task: Task) : TaskParseResult()
    data class Failure(val reason: String) : TaskParseResult()
}