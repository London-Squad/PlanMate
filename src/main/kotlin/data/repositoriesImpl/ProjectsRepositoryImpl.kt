package data.repositoriesImpl

import data.csvDataSource.dtoMappers.*
import data.dataSources.ProjectsDataSource
import data.dataSources.TasksDataSource
import data.dataSources.TasksStatesDataSource
import logic.entities.Project
import logic.exceptions.ProjectNotFoundException
import logic.exceptions.TaskStateNotFoundException
import logic.repositories.ProjectsRepository
import java.util.*

class ProjectsRepositoryImpl(
    private val projectsDataSource: ProjectsDataSource,
    private val tasksStatesDataSource: TasksStatesDataSource,
    private val tasksDataSource: TasksDataSource
) : ProjectsRepository {

    override fun getAllProjects(includeDeleted: Boolean): List<Project> {
        val projectsData = projectsDataSource.getAllProjects()
            .filter { if (includeDeleted) true else !it.isDeleted }
        val tasksData = tasksDataSource.getAllTasks()
            .filter { if (includeDeleted) true else !it.isDeleted }
        val taskStatesData = tasksStatesDataSource.getAllTasksStates()
            .filter { if (includeDeleted) true else !it.isDeleted }

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
        project.tasks
            .map { task -> task.toTaskDto(project.id) }
            .forEach(tasksDataSource::addNewTask)
        project.tasksStates
            .map { taskState -> taskState.toTaskStateDto(project.id) }
            .forEach(tasksStatesDataSource::addNewTaskState)
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