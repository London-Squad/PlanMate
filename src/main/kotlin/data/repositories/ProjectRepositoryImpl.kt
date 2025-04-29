package data.repositories

import logic.entities.Project
import logic.repositories.ProjectsRepository
import java.util.UUID

class ProjectRepositoryImpl: ProjectsRepository {
    override fun getAllProjects(): List<Project> {
        TODO("Not yet implemented")
    }

    override fun addNewProject(project: Project) {
        TODO("Not yet implemented")
    }

    override fun editProjectTitle(projectId: UUID, newTitle: String) {
        TODO("Not yet implemented")
    }

    override fun editProjectDescription(projectId: UUID, newDescription: String) {
        TODO("Not yet implemented")
    }

    override fun deleteProject(projectId: UUID) {
        TODO("Not yet implemented")
    }
}