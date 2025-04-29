package logic.useCases

import logic.entities.Project
import logic.repositories.ProjectsRepository
import java.util.UUID

class ProjectUseCases(private val projectsRepository: ProjectsRepository) {

    fun getAllProjects(): List<Project> {
        return projectsRepository.getAllProjects()
    }

    fun getProjectById(projectId: UUID): Project? {
        return projectsRepository.getAllProjects().find { it.id == projectId }
    }

    fun createProject(title: String, description: String): Project {
        val project = Project(
            id = UUID.randomUUID(),
            title = title,
            description = description,
            tasks = emptyList(),
            states = emptyList()
        )
        projectsRepository.addNewProject(project)
        return project
    }

    fun editProjectTitle(projectId: UUID, newTitle: String) {
        projectsRepository.editProjectTitle(projectId, newTitle)
    }

    fun editProjectDescription(projectId: UUID, newDescription: String) {
        projectsRepository.editProjectDescription(projectId, newDescription)
    }

    fun deleteProject(projectId: UUID) {
        projectsRepository.deleteProject(projectId)
    }
}
