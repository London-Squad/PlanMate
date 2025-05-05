package logic.repositories

import logic.entities.Project
import java.util.UUID

interface ProjectsRepository {

    fun getAllProjects(): List<Project>

    fun getProjectById(projectId: UUID): Project

    fun addNewProject(project: Project): Project

    fun editProjectTitle(projectId: UUID, newTitle: String)

    fun editProjectDescription(projectId: UUID, newDescription: String)

    fun deleteProject(projectId: UUID)
}