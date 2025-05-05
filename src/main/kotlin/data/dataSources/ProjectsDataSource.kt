package data.dataSources

import data.entitiesData.ProjectData
import java.util.*

interface ProjectsDataSource {
    fun getAllProjects(): List<ProjectData>
    fun addNewProject(project: ProjectData)
    fun editProjectTitle(projectId: UUID, newTitle: String)
    fun editProjectDescription(projectId: UUID, newDescription: String)
    fun deleteProject(projectId: UUID)
}