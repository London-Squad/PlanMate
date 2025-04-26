package data.repository

import logic.ProjectsRepository
import model.Project

class FakeProjectsRepositoryImpl: ProjectsRepository {
    override fun getAllProjects(): List<Project> {
        TODO("Not yet implemented")
    }
}