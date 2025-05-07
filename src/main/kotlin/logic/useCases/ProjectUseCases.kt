package logic.useCases

import logic.entities.*
import logic.repositories.AuthenticationRepository
import logic.repositories.LogsRepository
import logic.repositories.ProjectsRepository
import logic.repositories.TasksStatesRepository
import java.util.*

class ProjectUseCases(
    private val projectsRepository: ProjectsRepository,
    private val tasksStatesRepository: TasksStatesRepository,
    private val logsRepository: LogsRepository,
    private val authenticationRepository: AuthenticationRepository,
) {

    fun getAllProjects(): List<Project> {
        return projectsRepository.getAllProjects()
    }

    fun getProjectById(projectId: UUID): Project {
        return projectsRepository.getProjectById(projectId)
    }

    fun createProject(title: String, description: String) {
        val projectId = UUID.randomUUID()
        val defaultTasksStates = tasksStatesRepository.getDefaultTaskStates(projectId)

        val project = Project(
            id = projectId,
            title = title,
            description = description,
            tasks = emptyList(),
            tasksStates = defaultTasksStates
        )
        projectsRepository.addNewProject(project)
        logNewProject(project)
    }

    private fun logNewProject(project: Project) {
        logsRepository.addLog(
            Log(
                user = authenticationRepository.getLoggedInUser(),
                action = Create(project)
            )
        )
    }

    fun editProjectTitle(projectId: UUID, newTitle: String) {
        val project = projectsRepository.getProjectById(projectId)

        projectsRepository.editProjectTitle(projectId, newTitle)

        logsRepository.addLog(
            Log(
                user = authenticationRepository.getLoggedInUser(),
                action = Edit(
                    entity = project,
                    property = "title",
                    oldValue = project.title,
                    newValue = newTitle
                )
            )
        )
    }

    fun editProjectDescription(projectId: UUID, newDescription: String) {
        val project = projectsRepository.getProjectById(projectId)

        projectsRepository.editProjectDescription(projectId, newDescription)

        logsRepository.addLog(
            Log(
                user = authenticationRepository.getLoggedInUser(),
                action = Edit(
                    entity = project,
                    property = "description",
                    oldValue = project.description,
                    newValue = newDescription
                )
            )
        )
    }

    fun deleteProject(projectId: UUID) {
        val project = projectsRepository.getProjectById(projectId)

        projectsRepository.deleteProject(projectId)

        logsRepository.addLog(
            Log(
                user = authenticationRepository.getLoggedInUser(),
                action = Delete(entity = project)
            )
        )
    }
}