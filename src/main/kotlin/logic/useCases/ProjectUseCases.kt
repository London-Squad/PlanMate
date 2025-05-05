package logic.useCases

import logic.entities.*
import logic.repositories.AuthenticationRepository
import logic.repositories.LogsRepository
import logic.repositories.ProjectsRepository
import logic.repositories.TaskRepository
import java.util.*

class ProjectUseCases(
    private val projectsRepository: ProjectsRepository,
    private val logsRepository: LogsRepository,
    private val authenticationRepository: AuthenticationRepository,
    private val taskRepository: TaskRepository
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
            tasksStates = defaultStates
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

    fun addNewTask(task: Task, projectId: UUID) {
        taskRepository.addNewTask(task, projectId)
    }

    fun logTaskCreation(task: Task) {
        logsRepository.addLog(
            Log(
                user = authenticationRepository.getLoggedInUser(),
                action = Create(task)
            )
        )
    }
}