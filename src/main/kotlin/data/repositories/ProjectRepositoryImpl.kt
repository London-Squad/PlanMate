package data.repositories

import data.csv.CsvProjectHandler
import logic.entities.Project
import logic.repositories.ProjectsRepository
import java.util.*

class ProjectRepositoryImpl(private val projectHandler: CsvProjectHandler) : ProjectsRepository {
    private val projectsInMemory: MutableList<Project> = mutableListOf()

    init {
        projectsInMemory.addAll(projectHandler.loadProjects())
    }

    override fun getAllProjects(): List<Project> {
        projectsInMemory.clear()
        projectsInMemory.addAll(projectHandler.loadProjects())
        return projectsInMemory.toList()
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