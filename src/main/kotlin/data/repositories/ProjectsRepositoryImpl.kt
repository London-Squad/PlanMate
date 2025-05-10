package data.repositories

import data.dto.ProjectDto
import data.repositories.dataSourceInterfaces.ProjectsDataSource
import data.repositories.dtoMappers.*
import logic.entities.Project
import logic.exceptions.ProjectNotFoundException
import logic.repositories.ProjectsRepository
import java.util.*

class ProjectsRepositoryImpl(
    private val projectsDataSource: ProjectsDataSource,
) : ProjectsRepository {

    override suspend fun getAllProjects(includeDeleted: Boolean): List<Project> {
        return projectsDataSource.getAllProjects(includeDeleted)
            .map(ProjectDto::toProject)
    }

    override suspend fun getProjectById(projectId: UUID, includeDeleted: Boolean): Project {
        return projectsDataSource.getAllProjects(includeDeleted)
            .firstOrNull { it.id == projectId }
            ?.toProject()
            ?: throw ProjectNotFoundException("Project with id $projectId not found")
    }

    override suspend fun addNewProject(project: Project) {
        projectsDataSource.addNewProject(
            project.toProjectDto()
        )
    }

    override suspend fun editProjectTitle(projectId: UUID, newTitle: String) {
        projectsDataSource.editProjectTitle(projectId, newTitle)
    }

    override suspend fun editProjectDescription(projectId: UUID, newDescription: String) {
        projectsDataSource.editProjectDescription(projectId, newDescription)
    }

    override suspend fun deleteProject(projectId: UUID) {
        projectsDataSource.deleteProject(projectId)
    }
}