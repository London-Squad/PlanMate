package logic.usecases

import logic.entities.Project
import logic.repositories.ProjectsRepository
import java.util.UUID

class ManageProjectUseCase(private val projectRepository: ProjectsRepository) {
    fun getAllProjects(): List<Project> {
        return projectRepository.getAllProjects()
    }

    fun addNewProject(project: Project) {
        projectRepository.addNewProject(project)
    }

    fun editProjectTitle(projectId: UUID, newTitle: String) {
        projectRepository.editProjectTitle(projectId, newTitle)
    }

    fun editProjectDescription(projectId: UUID, newDescription: String) {
        projectRepository.editProjectDescription(projectId, newDescription)
    }

    fun deleteProject(projectId: UUID) {
        projectRepository.deleteProject(projectId)
    }
}