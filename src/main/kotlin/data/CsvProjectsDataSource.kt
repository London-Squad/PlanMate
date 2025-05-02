package data

import logic.entities.Project
import logic.repositories.ProjectsRepository
import java.io.File
import java.util.UUID

class CsvProjectsDataSource(
    private val file: File,
    private val csvTasksDataSource: CsvTasksDataSource,
    private val csvStatesDataSource: CsvStatesDataSource
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
        return file.readLines().drop(1).mapNotNull { line ->
            // Split the line into parts, taking only the first 3 fields
            val parts = line.split(",", limit = 3)
            if (parts.size < 3) return@mapNotNull null
            try {
                val projectId = UUID.fromString(parts[0].trim())
                Project(
                    id = projectId,
                    title = parts[1].trim(),
                    description = parts[2].trim(),
                    tasks = csvTasksDataSource.getAllTasksByProjectID(projectId),
                    states = csvStatesDataSource.getAllStatesByProjectId(projectId)
                )
            } catch (e: IllegalArgumentException) {
                null
            }
        }
    }

    override fun addNewProject(project: Project) {
        project.states.forEach { state ->
            csvStatesDataSource.addNewState(state, project.id)
        }
        project.tasks.forEach { task ->
            csvTasksDataSource.addNewTask(task, project.id)
        }
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
        csvTasksDataSource.getAllTasksByProjectID(projectId).forEach { task ->
            csvTasksDataSource.deleteTask(task.id)
        }
        csvStatesDataSource.getAllStatesByProjectId(projectId).forEach { state ->
            csvStatesDataSource.deleteState(state.id)
        }
        writeProjectsToFile(projects)
    }

    private fun writeProjectsToFile(projects: List<Project>) {
        file.writeText("id,title,description\n")
        projects.forEach { project ->
            file.appendText("${project.id},${project.title},${project.description}\n")
        }
    }
}