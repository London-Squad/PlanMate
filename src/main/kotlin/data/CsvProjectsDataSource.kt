package data

import data.csvHandler.CsvFileHandler
import data.parser.ProjectParseResult
import data.parser.ProjectParser
import logic.entities.Project
import logic.repositories.ProjectsRepository
import logic.repositories.StatesRepository
import logic.repositories.TaskRepository
import java.util.UUID

class CsvProjectsDataSource(
    private val fileHandler: CsvFileHandler,
    private val projectParser: ProjectParser,
    private val taskRepository: TaskRepository,
    private val statesRepository: StatesRepository
) : ProjectsRepository {

    init {
        fileHandler.writeHeader("id,title,description")
    }

    override fun getAllProjects(): List<Project> =
        fileHandler.readLines().drop(1).mapNotNull { line ->
            when (val result = projectParser.parseProjectLine(line)) {
                is ProjectParseResult.Success -> result.project
                is ProjectParseResult.Failure -> null
            }
        }

    private fun updateProjects(transform: (List<Project>) -> List<Project>) {
        val header = fileHandler.readLines().firstOrNull() ?: "id,title,description"
        val projects = getAllProjects()
        val updatedProjects = transform(projects)
        fileHandler.rewriteLines(
            listOf(header) +
                    updatedProjects.map { projectParser.formatProjectLine(it) }
        )
    }

    override fun addNewProject(project: Project) {
        project.states.forEach { statesRepository.addNewState(it, project.id) }
        project.tasks.forEach { taskRepository.addNewTask(it, project.id) }
        fileHandler.appendLine(projectParser.formatProjectLine(project))
    }

    override fun editProjectTitle(projectId: UUID, newTitle: String) =
        updateProjects { projects -> projects.map { if (it.id == projectId) it.copy(title = newTitle) else it } }

    override fun editProjectDescription(projectId: UUID, newDescription: String) =
        updateProjects { projects -> projects.map { if (it.id == projectId) it.copy(description = newDescription) else it } }

    override fun deleteProject(projectId: UUID) {
        val tasksToDelete = taskRepository.getAllTasksByProjectID(projectId)
        tasksToDelete.forEach { taskRepository.deleteTask(it.id) }

        val statesToDelete = statesRepository.getAllStatesByProjectId(projectId)
        statesToDelete.forEach { statesRepository.deleteState(it.id) }

        updateProjects { projects -> projects.filter { it.id != projectId } }
    }
}