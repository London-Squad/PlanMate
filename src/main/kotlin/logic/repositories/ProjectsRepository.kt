package logic.repositories

import logic.entities.Project
import java.util.*

interface ProjectsRepository {
    suspend fun getAllProjects(includeDeleted: Boolean = false): List<Project>
    suspend fun getProjectById(projectId: UUID, includeDeleted: Boolean = false): Project
    suspend fun addNewProject(project: Project)
    suspend fun editProjectTitle(projectId: UUID, newTitle: String)
    suspend fun editProjectDescription(projectId: UUID, newDescription: String)
    suspend fun deleteProject(projectId: UUID)
}