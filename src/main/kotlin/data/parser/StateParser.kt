package data.parser

import logic.entities.State
import java.util.UUID

class StateParser {
    fun parseStateLine(line: String): StateParseResult {
        return try {
            val (id, title, description, projectIdStr) = line.split(",", limit = 4)
            val projectId = UUID.fromString(projectIdStr)
            StateParseResult.Success(
                projectId,
                State(
                    id = UUID.fromString(id),
                    title = title,
                    description = description
                )
            )
        } catch (e: Exception) {
            StateParseResult.Failure("Failed to parse state line: $line, reason: ${e.message}")
        }
    }

    fun formatStateLine(state: State, projectId: UUID): String =
        "${state.id},${state.title},${state.description},${projectId}"
}

sealed class StateParseResult {
    data class Success(val projectId: UUID, val state: State) : StateParseResult()
    data class Failure(val reason: String) : StateParseResult()
}