package data.csv
import logic.entities.Project
import java.io.File
import java.util.UUID
// temporary file
class CsvProjectHandler(private val filePath: String) {

    fun loadProjects(): List<Project> {
        val projects = mutableListOf<Project>()
        val lines = File(filePath).takeIf { it.exists() }?.readLines().orEmpty()
        projects.addAll(lines.mapNotNull(::deserialize))
        return projects
    }

    fun saveProject(project: Project) {
        val line = serialize(project)
        val file = File(filePath)
        if (!file.exists()) {
            file.createNewFile()
        }
        file.appendText(line + "\n")
    }

    fun deleteProject(projectId: UUID) {
        val file = File(filePath)
        if (file.exists()) {
            val newLines = file.readLines().filterNot {
                val project = deserialize(it)
                (project?.id ?: "") == projectId
            }
            file.writeText(newLines.joinToString("\n") + if (newLines.isNotEmpty()) "\n" else "")
        }
    }

    fun updateProject(updated: Project) {
        val file = File(filePath)
        if (!file.exists()) {
            return
        }
        val newLines = file.readLines().map {
            val project = deserialize(it)
            if ((project?.id ?: "") == updated.id) serialize(updated) else it
        }
        file.writeText(newLines.joinToString("\n") + if (newLines.isNotEmpty()) "\n" else "")
    }

    private fun serialize(project: Project): String =
        listOf(
            project.id.toString(),
            project.title.replace(",", " "),
            project.description.replace(",", " ")
        ).joinToString(",")

    private fun deserialize(line: String): Project? {
        val parts = line.split(",", limit = 3)
        return try {
            Project(
                id = UUID.fromString(parts[0]),
                title = parts[1],
                description = parts[2],
                tasks = emptyList(),
                states = emptyList()
            )
        } catch (e: Exception) {
            null
        }
    }
}