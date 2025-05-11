package logic.repositories

import logic.entities.Project
import java.util.UUID

interface ProjectsRepository {
    fun getAllProjects(includeDeleted: Boolean = false): List<Project>
    fun getProjectById(projectId: UUID, includeDeleted: Boolean = false): Project
    fun addNewProject(project: Project)
    fun editProjectTitle(projectId: UUID, newTitle: String)
    fun editProjectDescription(projectId: UUID, newDescription: String)
    fun deleteProject(projectId: UUID)
}