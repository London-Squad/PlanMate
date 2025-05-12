package data.repositories.dataSourceInterfaces

import data.dto.ProjectDto
import java.util.*

interface ProjectsDataSource {
    suspend fun getAllProjects(includeDeleted: Boolean): List<ProjectDto>
    suspend fun addNewProject(project: ProjectDto)
    suspend fun editProjectTitle(projectId: UUID, newTitle: String)
    suspend fun editProjectDescription(projectId: UUID, newDescription: String)
    suspend fun deleteProject(projectId: UUID)
}