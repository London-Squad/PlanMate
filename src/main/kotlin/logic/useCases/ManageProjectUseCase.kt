package logic.useCases

import logic.entities.*
import logic.repositories.ProjectsRepository
import java.util.*

class ManageProjectUseCase(
    private val projectsRepository: ProjectsRepository,
    private val createLogUseCase: CreateLogUseCase
) {

    fun getAllProjects(): List<Project> {
        return projectsRepository.getAllProjects()
    }

    fun getProjectById(projectId: UUID): Project {
        return projectsRepository.getProjectById(projectId)
    }

    fun editProjectTitle(projectId: UUID, newTitle: String) {
        val projectBeforeEdition = projectsRepository.getProjectById(projectId)

        projectsRepository.editProjectTitle(projectId, newTitle)
        createLogUseCase.logEntityTitleEdition(
            projectId,
            projectBeforeEdition.title,
            newTitle
        )
    }

    fun editProjectDescription(projectId: UUID, newDescription: String) {
        val projectBeforeEdition = projectsRepository.getProjectById(projectId)

        projectsRepository.editProjectDescription(projectId, newDescription)
        createLogUseCase.logEntityDescriptionEdition(
            projectId,
            projectBeforeEdition.description,
            newDescription
        )
    }

    fun deleteProject(projectId: UUID) {
        projectsRepository.deleteProject(projectId)
        createLogUseCase.logEntityDeletion(projectId)
    }
}