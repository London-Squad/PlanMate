package data.parser

import data.csvHandler.CsvFileHandler
import logic.entities.State
import java.util.UUID

class StateParser(private val csvHandler: CsvFileHandler) {
    fun parseStateLine(line: String): StateParseResult {
        return try {
            val parts = csvHandler.decodeRecord(line)
            if (parts.size < 4) return StateParseResult.Failure("Invalid state line format: $line")

            val id = parts[0]
            val title = parts[1]
            val description = parts[2]
            val projectIdStr = parts[3]

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

    fun formatStateLine(state: State, projectId: UUID): String {
        val record = listOf(
            state.id.toString(),
            state.title,
            state.description,
            projectId.toString()
        )
        return csvHandler.encodeRecord(record)
    }
}

sealed class StateParseResult {
    data class Success(val projectId: UUID, val state: State) : StateParseResult()
    data class Failure(val reason: String) : StateParseResult()
}