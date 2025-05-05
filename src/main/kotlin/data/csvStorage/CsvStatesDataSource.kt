package data.csvStorage

import logic.entities.State
import logic.repositories.StatesRepository
import java.io.File
import java.util.UUID

class CsvStatesDataSource(
    private val file: File
) : StatesRepository {

    init {
        val directory = file.parentFile
        if (!directory.exists()) {
            directory.mkdir()
        }

        if (!file.exists()) {
            file.createNewFile()
            file.writeText("id,title,description,projectId\n")
        }
    }

    override fun getAllStatesByProjectId(projectId: UUID): List<State> {
        return file.readLines().drop(1).mapNotNull { line ->
            val (id, title, description, projectIdStr) = line.split(",", limit = 4)
            if (projectIdStr == projectId.toString()) {
                State(
                    id = UUID.fromString(id),
                    title = title,
                    description = description
                )
            } else {
                null
            }
        }
    }

    override fun getStateById(stateId: UUID): State {
        return file.readLines().drop(1).find { line ->
            val (id, _, _, _) = line.split(",", limit = 4)
            id == stateId.toString()
        }?.let { line ->
            val (id, title, description, _) = line.split(",", limit = 4)
            State(
                id = UUID.fromString(id),
                title = title,
                description = description
            )
        } ?: State.NoState
    }

    override fun addNewState(state: State, projectId: UUID) {
        file.appendText("${state.id},${state.title},${state.description},${projectId}\n")
    }

    override fun editStateTitle(stateId: UUID, newTitle: String) {
        val lines = file.readLines()
        val updatedLines = lines.map { line ->
            val (id, _, _, projectId) = line.split(",", limit = 4)
            if (id == stateId.toString()) {
                "$id,$newTitle,${line.split(",", limit = 4)[2]},$projectId"
            } else {
                line
            }
        }
        file.writeText("")
        updatedLines.forEach { file.appendText("$it\n") }
    }

    override fun editStateDescription(stateId: UUID, newDescription: String) {
        val lines = file.readLines()
        val updatedLines = lines.map { line ->
            val (id, title, _, projectId) = line.split(",", limit = 4)
            if (id == stateId.toString()) {
                "$id,$title,$newDescription,$projectId"
            } else {
                line
            }
        }
        file.writeText("")
        updatedLines.forEach { file.appendText("$it\n") }
    }

    override fun deleteState(stateId: UUID) {
        val lines = file.readLines()
        val updatedLines = lines.filter { line ->
            val (id, _, _, _) = line.split(",", limit = 4)
            id != stateId.toString()
        }
        file.writeText("")
        updatedLines.forEach { file.appendText("$it\n") }
    }
}