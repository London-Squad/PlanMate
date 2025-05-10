package logic.useCases

import logic.entities.*
import logic.repositories.ProjectsRepository
import java.util.*

class ManageProjectUseCase(
    private val projectsRepository: ProjectsRepository,
    private val createLogUseCase: CreateLogUseCase
) {

    suspend fun getAllProjects(): List<Project> {
        return projectsRepository.getAllProjects()
    }

    suspend fun getProjectById(projectId: UUID): Project {
        return projectsRepository.getProjectById(projectId)
    }

    suspend fun editProjectTitle(projectId: UUID, newTitle: String) {
        val projectBeforeEdition = projectsRepository.getProjectById(projectId)

        projectsRepository.editProjectTitle(projectId, newTitle)
        createLogUseCase.logEntityTitleEdition(
            projectBeforeEdition,
            projectBeforeEdition.title,
            newTitle
        )
    }

    suspend fun editProjectDescription(projectId: UUID, newDescription: String) {
        val projectBeforeEdition = projectsRepository.getProjectById(projectId)

        projectsRepository.editProjectDescription(projectId, newDescription)
        createLogUseCase.logEntityDescriptionEdition(
            projectBeforeEdition,
            projectBeforeEdition.description,
            newDescription
        )
    }

    suspend fun deleteProject(projectId: UUID) {
        val project = projectsRepository.getProjectById(projectId)

        projectsRepository.deleteProject(projectId)
        createLogUseCase.logEntityDeletion(project)
    }
}