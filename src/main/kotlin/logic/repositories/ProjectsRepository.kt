package logic.repositories

import logic.entities.Admin
import logic.entities.Project
import logic.entities.State
import logic.entities.Task
import java.util.UUID

interface ProjectsRepository {

    fun getAllProjects(): List<Project>

    fun addNewProject(project: Project, admin: Admin)

    fun addNewState(state: State, project: Project)

    fun addNewTask(task: Task, project: Project)

    fun changeStateOfTask(task: Task, newState: State)

    fun deleteProject(projectId: UUID)

    fun deleteState(stateId: UUID)

    fun deleteTask(tasktId: UUID)

    fun editProject(projectId: UUID)

    fun editState(stateId: UUID)

    fun editTask(taskId: UUID)
}