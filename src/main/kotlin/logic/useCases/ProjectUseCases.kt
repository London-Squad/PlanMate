package logic.useCases

import logic.entities.*
import logic.repositories.CacheDataRepository
import logic.repositories.LogsRepository
import logic.repositories.ProjectsRepository
import java.time.LocalDateTime
import java.util.UUID

class ProjectUseCases(
    private val projectsRepository: ProjectsRepository,
    private val cacheDataRepository: CacheDataRepository,
    private val logsRepository: LogsRepository
) {

    fun getAllProjects(): List<Project> {
        return projectsRepository.getAllProjects()
    }

    fun getProjectById(projectId: UUID): Project? {
        return projectsRepository.getAllProjects().find { it.id == projectId }
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
        logsRepository.addLog(
            Log(
                user = cacheDataRepository.getLoggedInUser(),
                time = LocalDateTime.now(),
                action = Create(project)
            )
        )
    }

    fun editProjectTitle(projectId: UUID, newTitle: String) {
        val project = getProjectById(projectId) ?: return

        logsRepository.addLog(
            Log(
                user = cacheDataRepository.getLoggedInUser(),
                time = LocalDateTime.now(),
                action = Edit(
                    entity = project,
                    property = "title",
                    oldValue = project.title,
                    newValue = newTitle
                )
            )
        )
        projectsRepository.editProjectTitle(projectId, newTitle)
    }

    fun editProjectDescription(projectId: UUID, newDescription: String) {
        val project = getProjectById(projectId) ?: return

        logsRepository.addLog(
            Log(
                user = cacheDataRepository.getLoggedInUser(),
                time = LocalDateTime.now(),
                action = Edit(
                    entity = project,
                    property = "description",
                    oldValue = project.description,
                    newValue = newDescription
                )
            )
        )

        projectsRepository.editProjectDescription(projectId, newDescription)
    }

    fun deleteProject(projectId: UUID) {
        val project = getProjectById(projectId) ?: return

        logsRepository.addLog(
            Log(
                user = cacheDataRepository.getLoggedInUser(),
                time = LocalDateTime.now(),
                action = Delete(
                    entity = project
                )
            )
        )

        projectsRepository.deleteProject(projectId)
    }

    fun updateProject(project: Project) {
        val existingProject = getProjectById(project.id)
        if (existingProject != null) {
            projectsRepository.deleteProject(project.id)
            projectsRepository.addNewProject(project)
        }
    }

    fun logTaskCreation(task: Task) {
        logsRepository.addLog(
            Log(
                user = cacheDataRepository.getLoggedInUser(),
                time = LocalDateTime.now(),
                action = Create(task)
            )
        )
    }
}