package data

import data.csvHandler.CsvFileHandler
import data.parser.StateParseResult
import data.parser.StateParser
import logic.entities.State
import logic.repositories.StatesRepository
import java.util.UUID

class CsvStatesDataSource(
    private val fileHandler: CsvFileHandler,
    private val stateParser: StateParser
) : StatesRepository {

    init {
        fileHandler.writeHeader("id,title,description,projectId")
    }

    private fun getAllStates(): List<Pair<UUID, State>> =
        fileHandler.readLines().drop(1).mapNotNull { line ->
            when (val result = stateParser.parseStateLine(line)) {
                is StateParseResult.Success -> Pair(result.projectId, result.state)
                is StateParseResult.Failure -> null
            }
        }

    private fun filterStatesByProjectId(projectId: UUID): List<State> =
        getAllStates().filter { it.first == projectId }.map { it.second }

    private fun updateStates(transform: (List<String>) -> List<String>) {
        val lines = fileHandler.readLines()
        // Preserve the header when updating
        val header = lines.firstOrNull() ?: "id,title,description,projectId"
        val dataLines = lines.drop(1)

        val transformedData = transform(dataLines)
        // Ensure the header is always the first line when rewriting
        fileHandler.rewriteLines(listOf(header) + transformedData)
    }

    override fun getAllStatesByProjectId(projectId: UUID): List<State> = filterStatesByProjectId(projectId)

    override fun getStateById(stateId: UUID): State =
        getAllStates().find { it.second.id == stateId }?.second ?: State.NoState

    override fun addNewState(state: State, projectId: UUID) {
        fileHandler.appendLine(stateParser.formatStateLine(state, projectId))
    }

    override fun editStateTitle(stateId: UUID, newTitle: String) =
        updateStates { lines ->
            lines.map { line ->
                val parts = line.split(",", limit = 4)
                if (parts.size >= 4 && parts[0] == stateId.toString()) {
                    "${stateId},${newTitle},${parts[2]},${parts[3]}"
                } else {
                    line
                }
            }
        }

    override fun editStateDescription(stateId: UUID, newDescription: String) =
        updateStates { lines ->
            lines.map { line ->
                val parts = line.split(",", limit = 4)
                if (parts.size >= 4 && parts[0] == stateId.toString()) {
                    "${stateId},${parts[1]},${newDescription},${parts[3]}"
                } else {
                    line
                }
            }
        }

    override fun deleteState(stateId: UUID) =
        updateStates { lines ->
            lines.filter {
                val parts = it.split(",", limit = 4)
                parts.isEmpty() || parts[0] != stateId.toString()
            }
        }
}