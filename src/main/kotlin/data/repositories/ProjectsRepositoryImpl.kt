package data.repositories

import data.dto.ProjectDto
import data.repositories.dataSourceInterfaces.ProjectsDataSource
import data.repositories.dataSourceInterfaces.TaskStatesDataSource
import data.repositories.dtoMappers.*
import logic.entities.Project
import logic.exceptions.ProjectNotFoundException
import logic.repositories.ProjectsRepository
import java.util.*

class ProjectsRepositoryImpl(
    private val projectsDataSource: ProjectsDataSource,
    private val taskStatesDataSource: TaskStatesDataSource,
) : ProjectsRepository {

    override fun getAllProjects(includeDeleted: Boolean): List<Project> {
        return projectsDataSource.getAllProjects(includeDeleted)
            .map(ProjectDto::toProject)
    }

    override fun getProjectById(projectId: UUID, includeDeleted: Boolean): Project {
        return projectsDataSource.getAllProjects(includeDeleted)
            .firstOrNull { it.id == projectId }
            ?.toProject()
            ?: throw ProjectNotFoundException("Project with id $projectId not found")
    }

    override fun addNewProject(project: Project) {
        projectsDataSource.addNewProject(
            project.toProjectDto()
        )
        taskStatesDataSource.createDefaultTaskStatesForProject(project.id)
    }

    override fun editProjectTitle(projectId: UUID, newTitle: String) {
        projectsDataSource.editProjectTitle(projectId, newTitle)
    }

    override fun editProjectDescription(projectId: UUID, newDescription: String) {
        projectsDataSource.editProjectDescription(projectId, newDescription)
    }

    override fun deleteProject(projectId: UUID) {
        projectsDataSource.deleteProject(projectId)
    }
}