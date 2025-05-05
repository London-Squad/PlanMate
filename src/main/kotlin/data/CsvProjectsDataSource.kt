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
        try {
            val directory = file.parentFile
            if (!directory.exists()) {
                directory.mkdirs()
            }
            if (!file.exists()) {
                file.createNewFile()
                file.writeText("id,title,description\n")
            }
        } catch (e: Exception) {
            println("Failed to initialize projects file: ${e.message}")
        }
    }

    override fun getAllProjects(): List<Project> {
        try {
            return file.readLines().drop(1).mapNotNull { line ->
                val parts = line.split(",", limit = 3)
                if (parts.size < 3) {
                    println("Invalid project line: $line")
                    return@mapNotNull null
                }
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
                    println("Failed to parse project: $line, error: ${e.message}")
                    null
                }
            }
        } catch (e: Exception) {
            println("Failed to read projects: ${e.message}")
            return emptyList()
        }
    }

    override fun getProjectById(projectId: UUID): Project? {
        try {
            return getAllProjects().find { it.id == projectId }
        } catch (e: Exception) {
            println("Failed to retrieve project with ID $projectId: ${e.message}")
            return null
        }
    }

    override fun addNewProject(project: Project): Project? {
        try {
            project.states.forEach { state ->
                csvStatesDataSource.addNewState(state, project.id)
            }
            project.tasks.forEach { task ->
                csvTasksDataSource.addNewTask(task, project.id)
            }
            file.appendText("${project.id},${project.title},${project.description}\n")
            return project
        } catch (e: Exception) {
            println("Failed to add project: ${e.message}")
            return null
        }
    }

    override fun editProjectTitle(projectId: UUID, newTitle: String): Boolean {
        try {
            val projects = getAllProjects()
            if (projects.none { it.id == projectId }) {
                println("Project with ID $projectId not found")
                return false
            }
            val updatedProjects = projects.map { project ->
                if (project.id == projectId) project.copy(title = newTitle) else project
            }
            writeProjectsToFile(updatedProjects)
            return true
        } catch (e: Exception) {
            println("Failed to edit project title: ${e.message}")
            return false
        }
    }

    override fun editProjectDescription(projectId: UUID, newDescription: String): Boolean {
        try {
            val projects = getAllProjects()
            if (projects.none { it.id == projectId }) {
                println("Project with ID $projectId not found")
                return false
            }
            val updatedProjects = projects.map { project ->
                if (project.id == projectId) project.copy(description = newDescription) else project
            }
            writeProjectsToFile(updatedProjects)
            return true
        } catch (e: Exception) {
            println("Failed to edit project description: ${e.message}")
            return false
        }
    }

    override fun deleteProject(projectId: UUID): Boolean {
        try {
            val projects = getAllProjects()
            if (projects.none { it.id == projectId }) {
                println("Project with ID $projectId not found")
                return false
            }
            csvTasksDataSource.getAllTasksByProjectID(projectId).forEach { task ->
                csvTasksDataSource.deleteTask(task.id)
            }
            csvStatesDataSource.getAllStatesByProjectId(projectId).forEach { state ->
                csvStatesDataSource.deleteState(state.id)
            }
            val remainingProjects = projects.filter { it.id != projectId }
            writeProjectsToFile(remainingProjects)
            return true
        } catch (e: Exception) {
            println("Failed to delete project: ${e.message}")
            return false
        }
    }

    private fun writeProjectsToFile(projects: List<Project>) {
        try {
            file.writeText("id,title,description\n")
            projects.forEach { project ->
                file.appendText("${project.id},${project.title},${project.description}\n")
            }
        } catch (e: Exception) {
            println("Failed to write projects to file: ${e.message}")
        }
    }
}