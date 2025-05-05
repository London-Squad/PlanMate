package logic.useCases

import logic.entities.*
import logic.repositories.AuthenticationRepository
import logic.repositories.LogsRepository
import logic.repositories.ProjectsRepository
import java.util.*

class ProjectUseCases(
    private val projectsRepository: ProjectsRepository,
    private val logsRepository: LogsRepository,
    private val authenticationRepository: AuthenticationRepository
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
                user = authenticationRepository.getLoggedInUser(),
                action = Create(project)
            )
        )
    }

    fun editProjectTitle(projectId: UUID, newTitle: String) {
        logsRepository.addLog(
            Log(
                user = authenticationRepository.getLoggedInUser(),
                action = Edit(
                    entity = projectsRepository.getAllProjects().first { it.id == projectId },
                    property = "title",
                    oldValue = projectsRepository.getAllProjects().first { it.id == projectId }.title,
                    newValue = newTitle
                )
            )
        )
        projectsRepository.editProjectTitle(projectId, newTitle)
    }

    fun editProjectDescription(projectId: UUID, newDescription: String) {
        logsRepository.addLog(
            Log(
                user = authenticationRepository.getLoggedInUser(),
                action = Edit(
                    entity = projectsRepository.getAllProjects().first { it.id == projectId },
                    property = "description",
                    oldValue = projectsRepository.getAllProjects().first { it.id == projectId }.description,
                    newValue = newDescription
                )
            )
        )

        projectsRepository.editProjectDescription(projectId, newDescription)
    }

    fun deleteProject(projectId: UUID) {
        logsRepository.addLog(
            Log(
                user = authenticationRepository.getLoggedInUser(),
                action = Delete(
                    entity = projectsRepository.getAllProjects().first { it.id == projectId },
                )
            )
        )

        projectsRepository.deleteProject(projectId)
    }

    fun updateProject(project: Project) {
        projectsRepository.deleteProject(project.id)
        projectsRepository.addNewProject(project)
    }

    fun logTaskCreation(task: Task) {
        logsRepository.addLog(
            Log(
                user = authenticationRepository.getLoggedInUser(),
                action = Create(task)
            )
        )
    }

    fun createTask(projectId: UUID, title: String, description: String): Project {
        val project = getProjectById(projectId) ?: throw IllegalArgumentException("Project not found")

        if (project.states.isEmpty()) {
            throw IllegalStateException("No states available for this project. Cannot create task.")
        }

        val defaultState = project.states.first()

        val newTask = Task(
            id = UUID.randomUUID(),
            title = title,
            description = description,
            state = defaultState
        )

        val updatedProject = project.copy(tasks = project.tasks + newTask)
        updateProject(updatedProject)
        logTaskCreation(newTask)

        return updatedProject
    }
}
