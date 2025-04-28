package data.repository

import logic.entities.Admin
import logic.entities.Project
import logic.entities.State
import logic.entities.Task
import logic.repositories.ProjectsRepository
import java.util.*

class ProjectsRepositoryImpl: ProjectsRepository {
    override fun getAllProjects(): List<Project> {
        TODO("Not yet implemented")
    }

    override fun addNewProject(project: Project, admin: Admin) {
        TODO("Not yet implemented")
    }

    override fun addNewState(state: State, project: Project) {
        TODO("Not yet implemented")
    }

    override fun addNewTask(task: Task, project: Project) {
        TODO("Not yet implemented")
    }

    override fun changeStateOfTask(task: Task, newState: State) {
        TODO("Not yet implemented")
    }

    override fun deleteProject(projectId: UUID) {
        TODO("Not yet implemented")
    }

    override fun deleteState(stateId: UUID) {
        TODO("Not yet implemented")
    }

    override fun deleteTask(tasktId: UUID) {
        TODO("Not yet implemented")
    }

    override fun editProject(projectId: UUID) {
        TODO("Not yet implemented")
    }

    override fun editState(stateId: UUID) {
        TODO("Not yet implemented")
    }

    override fun editTask(taskId: UUID) {
        TODO("Not yet implemented")
    }
}