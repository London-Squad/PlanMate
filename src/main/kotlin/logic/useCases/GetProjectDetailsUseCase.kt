package logic.useCases

import logic.entities.Project
import logic.entities.Task
import logic.entities.TaskState
import logic.repositories.ProjectsRepository
import logic.repositories.TaskRepository
import logic.repositories.TaskStatesRepository
import java.util.*

class GetProjectDetailsUseCase(
    private val projectsRepository: ProjectsRepository,
    private val taskStatesRepository: TaskStatesRepository,
    private val taskRepository: TaskRepository
) {
    data class ProjectDetails(
        val project: Project,
        val tasks: List<Task>,
        val taskStates: List<TaskState>
    )

    suspend operator fun invoke(projectId: UUID, includeDeleted: Boolean = false): ProjectDetails {
        val project = projectsRepository.getProjectById(projectId, includeDeleted)
        val taskStates = taskStatesRepository.getTaskStatesByProjectId(projectId, includeDeleted)
        val tasks = taskRepository.getTasksByProjectID(projectId, includeDeleted)
        return ProjectDetails(project, tasks, taskStates)
    }
}