package data.repositoriesImpl

import data.dataSources.ProjectsDataSource
import data.dataSources.TasksDataSource
import data.dataSources.TasksStatesDataSource
import data.entitiesData.DtoMapper
import logic.entities.Project
import logic.exceptions.TaskStateNotFoundException
import logic.repositories.ProjectsRepository
import java.util.*

class ProjectsRepositoryImpl(
    private val projectsDataSource: ProjectsDataSource,
    private val tasksStatesDataSource: TasksStatesDataSource,
    private val tasksDataSource: TasksDataSource,
    private val mapper: DtoMapper
) : ProjectsRepository {

    override fun getAllProjects(): List<Project> {
        val projectsData = projectsDataSource.getAllProjects()
        val tasksData = tasksDataSource.getAllTasks()
        val taskStatesData = tasksStatesDataSource.getAllTasksStates()

        return projectsData
            .map { projectData ->
                val taskStatesOfProject = taskStatesData
                    .filter { it.projectId == projectData.id }
                    .map(mapper::mapToTaskState)

                val tasksOfProject = tasksData
                    .filter { it.projectId == projectData.id }
                    .map { taskData ->
                        mapper.mapToTask(
                            taskData,
                            taskStatesOfProject
                                .firstOrNull() { it.id == taskData.stateId } ?: throw TaskStateNotFoundException()
                        )
                    }

                mapper.mapToProject(projectData, tasksOfProject, taskStatesOfProject)
            }
    }

    override fun addNewProject(project: Project) {
        projectsDataSource.addNewProject(
            mapper.mapToProjectData(project)
        )
        project.tasks
            .map({ task -> mapper.mapToTaskData(task, project.id) })
            .forEach(tasksDataSource::addNewTask)
        project.tasksStates
            .map({ taskState -> mapper.mapToTaskStateData(taskState, project.id) })
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