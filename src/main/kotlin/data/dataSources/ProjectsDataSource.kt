package data.dataSources

import data.dto.ProjectDto
import java.util.*

interface ProjectsDataSource {
    fun getAllProjects(): List<ProjectDto>
    fun addNewProject(project: ProjectDto)
    fun editProjectTitle(projectId: UUID, newTitle: String)
    fun editProjectDescription(projectId: UUID, newDescription: String)
    fun deleteProject(projectId: UUID)
}