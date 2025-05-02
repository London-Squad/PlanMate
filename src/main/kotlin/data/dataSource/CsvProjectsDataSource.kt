package data.dataSource

import logic.entities.Project
import logic.entities.State
import logic.entities.Task
import logic.repositories.ProjectsRepository
import java.io.File
import java.util.UUID

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
            file.writeText("id,title,description,tasks,states\n")
        }
    }

    override fun getAllProjects(): List<Project> {
        return file.readLines().drop(1).mapNotNull { line ->
            val parts = line.split(",", limit = 5)
            if (parts.size < 5) return@mapNotNull null // Skip malformed lines
            val tasks = if (parts[3].isNotEmpty()) {
                parts[3].split(";").mapNotNull { taskStr ->
                    val taskParts = taskStr.split("|")
                    if (taskParts.size < 4) return@mapNotNull null
                    Task(
                        id = UUID.fromString(taskParts[0]),
                        title = taskParts[1],
                        description = taskParts[2],
                        state = State(
                            id = UUID.fromString(taskParts[3]),
                            title = "No State", // Simplified; real app would fetch from StatesRepository
                            description = "Default state"
                        )
                    )
                }
            } else {
                emptyList()
            }
            val states = if (parts[4].isNotEmpty()) {
                parts[4].split(";").mapNotNull { stateStr ->
                    val stateParts = stateStr.split("|")
                    if (stateParts.size < 3) return@mapNotNull null
                    State(
                        id = UUID.fromString(stateParts[0]),
                        title = stateParts[1],
                        description = stateParts[2]
                    )
                }
            } else {
                emptyList()
            }
            Project(
                id = UUID.fromString(parts[0]),
                title = parts[1],
                description = parts[2],
                tasks = tasks,
                states = states
            )
        }
    }

    override fun addNewProject(project: Project) {
        val tasksStr = project.tasks.joinToString(";") { task ->
            "${task.id}|${task.title}|${task.description}|${task.state.id}"
        }
        val statesStr = project.states.joinToString(";") { state ->
            "${state.id}|${state.title}|${state.description}"
        }
        file.appendText("${project.id},${project.title},${project.description},$tasksStr,$statesStr\n")
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
        file.writeText("id,title,description,tasks,states\n")
        projects.forEach { project ->
            val tasksStr = project.tasks.joinToString(";") { task ->
                "${task.id}|${task.title}|${task.description}|${task.state.id}"
            }
            val statesStr = project.states.joinToString(";") { state ->
                "${state.id}|${state.title}|${state.description}"
            }
            file.appendText("${project.id},${project.title},${project.description},$tasksStr,$statesStr\n")
        }
    }
}