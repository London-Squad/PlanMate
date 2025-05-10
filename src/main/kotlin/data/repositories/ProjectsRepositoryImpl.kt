package data.repositories

import data.repositories.dataSourceInterfaces.ProjectsDataSource
import data.repositories.dataSourceInterfaces.TaskStatesDataSource
import data.repositories.dataSourceInterfaces.TasksDataSource
import data.repositories.dtoMappers.*
import logic.entities.Project
import logic.exceptions.ProjectNotFoundException
import logic.exceptions.TaskStateNotFoundException
import logic.repositories.ProjectsRepository
import java.util.*

class ProjectsRepositoryImpl(
    private val projectsDataSource: ProjectsDataSource,
    private val taskStatesDataSource: TaskStatesDataSource,
    private val tasksDataSource: TasksDataSource
) : ProjectsRepository {

    override fun getAllProjects(includeDeleted: Boolean): List<Project> {
        val projectsData = projectsDataSource.getAllProjects(includeDeleted)
        val tasksData = tasksDataSource.getAllTasks(includeDeleted)
        val taskStatesData = taskStatesDataSource.getAllTasksStates(includeDeleted)

        return projectsData
            .map { projectData ->
                val taskStatesOfProject = taskStatesData
                    .filter { it.projectId == projectData.id }
                    .map { it.toTaskState() }

                val tasksOfProject = tasksData
                    .filter { it.projectId == projectData.id }
                    .map { taskData ->
                        taskData.toTask(
                            taskStatesOfProject
                                .firstOrNull { it.id == taskData.stateId } ?: throw TaskStateNotFoundException()
                        )
                    }

                projectData.toProject(tasksOfProject, taskStatesOfProject)
            }
    }

    override fun getProjectById(projectId: UUID, includeDeleted: Boolean): Project {
        return getAllProjects(includeDeleted).firstOrNull { it.id == projectId }
            ?: throw ProjectNotFoundException("Project with id $projectId not found")
    }

    override fun addNewProject(project: Project) {
        projectsDataSource.addNewProject(
            project.toProjectDto()
        )
        taskStatesDataSource.createDefaultTaskStatesForProject(project.id)
    }

    override fun editProjectTitle(projectId: UUID, newTitle: String) {
        projectsDataSource.editProjectTitle(projectId, newTitle)
    }

    override fun editProjectDescription(projectId: UUID, newDescription: String) {
        projectsDataSource.editProjectDescription(projectId, newDescription)
    }

    override fun deleteProject(projectId: UUID) {
        projectsDataSource.deleteProject(projectId)
    }
}