package data.parser

import logic.entities.Task
import logic.repositories.StatesRepository
import java.util.UUID


class TaskParser(private val statesRepository: StatesRepository) {
    fun parseTaskLine(line: String): TaskParseResult {
        return try {
            val (id, title, description, stateId, projectId) = line.split(",", limit = 5)
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

    fun formatTaskLine(projectId: UUID, task: Task): String =
        "${task.id},${task.title},${task.description},${task.state.id},${projectId}"
}

sealed class TaskParseResult {
    data class Success(val projectId: UUID, val task: Task) : TaskParseResult()
    data class Failure(val reason: String) : TaskParseResult()
}