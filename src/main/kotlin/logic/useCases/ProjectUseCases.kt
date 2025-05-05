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
        return projectsRepository.getProjectById(projectId)
    }

    fun createProject(title: String, description: String): Project? {
        val defaultStates = listOf(
            State(id = UUID.randomUUID(), title = "TODO", description = "TO DO TASKS"),
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
        val addedProject = projectsRepository.addNewProject(project)
        if (addedProject != null) {
            logNewProject(project)
            return project
        }
        return null
    }

    private fun logNewProject(project: Project) {
        logsRepository.addLog(
            Log(
                user = authenticationRepository.getLoggedInUser(),
                action = Create(project)
            )
        )
    }

    fun editProjectTitle(projectId: UUID, newTitle: String): Boolean {
        val project = projectsRepository.getProjectById(projectId) ?: return false
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
        return projectsRepository.editProjectTitle(projectId, newTitle)
    }

    fun editProjectDescription(projectId: UUID, newDescription: String): Boolean {
        val project = projectsRepository.getProjectById(projectId) ?: return false
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
        return projectsRepository.editProjectDescription(projectId, newDescription)
    }

    fun deleteProject(projectId: UUID): Boolean {
        val project = projectsRepository.getProjectById(projectId) ?: return false
        logsRepository.addLog(
            Log(
                user = authenticationRepository.getLoggedInUser(),
                action = Delete(entity = project)
            )
        )
        return projectsRepository.deleteProject(projectId)
    }

    fun updateProject(project: Project): Boolean {
        val deleted = projectsRepository.deleteProject(project.id)
        val added = projectsRepository.addNewProject(project)
        return deleted && added != null
    }

    private fun logTaskCreation(task: Task) {
        logsRepository.addLog(
            Log(
                user = authenticationRepository.getLoggedInUser(),
                action = Create(task)
            )
        )
    }

    fun createTask(projectId: UUID, taskTitle: String, taskDescription: String): Project? {
        val project = getProjectById(projectId) ?: return null

        if (project.states.isEmpty()) {
            return null
        }

        val defaultState = project.states.first()
        val newTask = Task(
            id = UUID.randomUUID(),
            title = taskTitle,
            description = taskDescription,
            state = defaultState
        )

        val updatedProject = project.copy(tasks = project.tasks + newTask)
        val updated = updateProject(updatedProject)
        if (updated) {
            logTaskCreation(newTask)
            return updatedProject
        }
        return null
    }
}