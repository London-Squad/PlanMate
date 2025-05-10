package logic.useCases

import logic.entities.Project
import logic.entities.Task
import logic.entities.TaskState
import logic.repositories.ProjectsRepository
import logic.repositories.TaskStatesRepository
import java.util.*

class CreateProjectUseCase(
    private val taskStatesRepository: TaskStatesRepository,
    private val projectsRepository: ProjectsRepository,
    private val createLogUseCase: CreateLogUseCase
    ) {

    suspend fun createProject(title: String, description: String) {
        val projectId = UUID.randomUUID()
        val tasks = emptyList<Task>()
        val defaultTasksStates = taskStatesRepository.getDefaultTaskStates(projectId)

        val project = buildNewProject(projectId, title, description, tasks, defaultTasksStates)

        projectsRepository.addNewProject(project)
        createLogUseCase.logEntityCreation(project)
    }

    private fun buildNewProject(
        id: UUID,
        title: String,
        description: String,
        tasks: List<Task>,
        taskStates: List<TaskState>,
    ): Project {
        return Project(
            id = id,
            title = title,
            description = description,
            tasks = tasks,
            tasksStates = taskStates
        )
    }
}