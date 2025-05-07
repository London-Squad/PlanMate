package logic.useCases

import logic.entities.*
import logic.repositories.ProjectsRepository
import logic.repositories.TaskStatesRepository
import java.util.*

class ManageProjectUseCase(
    private val projectsRepository: ProjectsRepository,
    private val taskStatesRepository: TaskStatesRepository,
    private val addLogUseCase: AddLogUseCase
) {

    fun getAllProjects(): List<Project> {
        return projectsRepository.getAllProjects()
    }

    fun getProjectById(projectId: UUID): Project {
        return projectsRepository.getProjectById(projectId)
    }

    fun addProject(title: String, description: String) {
        val projectId = UUID.randomUUID()
        val tasks = emptyList<Task>()
        val defaultTasksStates = taskStatesRepository.getDefaultTaskStates(projectId)

        val project = buildNewProject(projectId, title, description, tasks, defaultTasksStates)

        projectsRepository.addNewProject(project)
        addLogUseCase.logEntityCreation(project)
    }

    private fun buildNewProject(
        id: UUID,
        title: String,
        description: String,
        tasks: List<Task>,
        taskStates: List<TaskState>,
    ): Project {
        return Project(
            id = id,
            title = title,
            description = description,
            tasks = tasks,
            tasksStates = taskStates
        )
    }


    fun editProjectTitle(projectId: UUID, newTitle: String) {
        val projectBeforeEdition = projectsRepository.getProjectById(projectId)

        projectsRepository.editProjectTitle(projectId, newTitle)
        addLogUseCase.logEntityTitleEdition(
            projectBeforeEdition,
            projectBeforeEdition.title,
            newTitle
        )
    }

    fun editProjectDescription(projectId: UUID, newDescription: String) {
        val projectBeforeEdition = projectsRepository.getProjectById(projectId)

        projectsRepository.editProjectDescription(projectId, newDescription)
        addLogUseCase.logEntityDescriptionEdition(
            projectBeforeEdition,
            projectBeforeEdition.description,
            newDescription
        )
    }

    fun deleteProject(projectId: UUID) {
        val project = projectsRepository.getProjectById(projectId)

        projectsRepository.deleteProject(projectId)
        addLogUseCase.logEntityDeletion(project)
    }
}