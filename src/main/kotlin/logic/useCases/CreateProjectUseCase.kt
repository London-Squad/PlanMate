package logic.useCases

import logic.entities.Project
import logic.repositories.ProjectsRepository
import logic.repositories.TaskStatesRepository
import java.util.*

class CreateProjectUseCase(
    private val taskStatesRepository: TaskStatesRepository,
    private val projectsRepository: ProjectsRepository,
    private val createLogUseCase: CreateLogUseCase
) {

    fun createProject(title: String, description: String) {
        val projectId = UUID.randomUUID()

        val project = buildNewProject(projectId, title, description)

        projectsRepository.addNewProject(project)
        createLogUseCase.logEntityCreation(projectId)

        taskStatesRepository.createProjectDefaultTaskStates(projectId)
    }

    private fun buildNewProject(
        id: UUID,
        title: String,
        description: String
    ): Project {
        return Project(
            id = id,
            title = title,
            description = description,
        )
    }
}