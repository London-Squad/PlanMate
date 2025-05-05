package data.mongoDBDataSource

import logic.entities.Project
import logic.repositories.ProjectsRepository
import java.util.*

class MongoDBProjectsDataSource() : ProjectsRepository {
    override fun getAllProjects(includeDeleted: Boolean): List<Project> {
        TODO("Not yet implemented")
    }

    override fun getProjectById(projectId: UUID, includeDeleted: Boolean): Project {
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

    companion object {
        private const val ID_FIELD = "id"
        private const val TITLE_FIELD = "title"
        private const val DESCRIPTION_FIELD = "description"
        private const val IS_DELETED_FIELD = "isDeleted"
    }
}