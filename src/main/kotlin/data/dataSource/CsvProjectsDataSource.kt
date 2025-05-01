package data.dataSource

import logic.entities.Project
import logic.entities.State
import logic.repositories.ProjectsRepository
import java.util.UUID
import java.io.File

class CsvProjectsDataSource(
    private val file: File
) : ProjectsRepository {

    init {
        val directory = file.parentFile
        if (!directory.exists()) {
            directory.mkdir()
        }

        if (!file.exists()) {
            file.createNewFile()
            file.writeText("id,title,description\n")
        }
    }

    override fun getAllProjects(): List<Project> {
        return file.readLines().drop(1).map { line ->
            val (id, title, description) = line.split(",", limit = 3)
            Project(
                id = UUID.fromString(id),
                title = title,
                description = description,
                tasks = emptyList(),
                states = listOf(State.NoState)
            )
        }
    }

    override fun addNewProject(project: Project) {
        file.appendText("${project.id},${project.title},${project.description}\n")
    }

    override fun editProjectTitle(projectId: UUID, newTitle: String) {
        val projects = getAllProjects().map { project ->
            if (project.id == projectId) project.copy(title = newTitle) else project
        }
        writeProjectsToFile(projects)
    }

    override fun editProjectDescription(projectId: UUID, newDescription: String) {
        val projects = getAllProjects().map { project ->
            if (project.id == projectId) project.copy(description = newDescription) else project
        }
        writeProjectsToFile(projects)
    }

    override fun deleteProject(projectId: UUID) {
        val projects = getAllProjects().filter { it.id != projectId }
        writeProjectsToFile(projects)
    }

    private fun writeProjectsToFile(projects: List<Project>) {
        file.writeText("id,title,description\n")
        projects.forEach { project ->
            file.appendText("${project.id},${project.title},${project.description}\n")
        }
    }
}