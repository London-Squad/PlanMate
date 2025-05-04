package logic.useCases

import logic.entities.*
import logic.repositories.LogsRepository
import logic.repositories.ProjectsRepository
import java.util.UUID

class ProjectUseCases(
    private val projectsRepository: ProjectsRepository,
    private val logsRepository: LogsRepository
) {

    fun getAllProjects(): List<Project> {
        return projectsRepository.getAllProjects()
    }

    fun getProjectById(projectId: UUID): Project {
        return projectsRepository.getAllProjects().first { it.id == projectId }
    }

    fun createProject(title: String, description: String): Project {
        val defaultStates = listOf(
            State(
                id = UUID.randomUUID(),
                title = "TODO",
                description = "TO DO TASKS"
            ),
            State(id = UUID.randomUUID(), title = "InProgress", description = "INPROGRESS TASKS"),
            State(id = UUID.randomUUID(), title = "Done", description = "FINISHED TASKS")
        )

        val project = Project(
            id = UUID.randomUUID(),
            title = title,
            description = description,
            tasks = emptyList(),
            states = defaultStates
        )
        projectsRepository.addNewProject(project)
        logNewProject(project)
        return project
    }

    private fun logNewProject(project: Project) {
        logsRepository.addCreationLog(project)
    }

    fun editProjectTitle(projectId: UUID, newTitle: String) {
        val project = getProjectById(projectId)

        projectsRepository.editProjectTitle(projectId, newTitle)

        logsRepository.addEditionLog(
            planEntity = project,
            planEntityPropertyToChange = "title",
            oldValue = project.title,
            newValue = newTitle
        )
    }

    fun editProjectDescription(projectId: UUID, newDescription: String) {
        val project = getProjectById(projectId)

        projectsRepository.editProjectDescription(projectId, newDescription)

        logsRepository.addEditionLog(
            planEntity = project,
            planEntityPropertyToChange = "description",
            oldValue = project.description,
            newValue = newDescription
        )
    }

    fun deleteProject(projectId: UUID) {
        val project = getProjectById(projectId)
        logsRepository.addDeletionLog(project)
        projectsRepository.deleteProject(projectId)
    }

    fun updateProject(project: Project) {
        projectsRepository.deleteProject(project.id)
        projectsRepository.addNewProject(project)
    }

    fun logTaskCreation(task: Task) {
        logsRepository.addCreationLog(task)
    }
}