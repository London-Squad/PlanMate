package logic.useCases

import data.dataSources.defaultTaskStatesTitleAndDescription
import logic.entities.Project
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

        val project = buildNewProject(projectId, title, description)

        projectsRepository.addNewProject(project)
        createLogUseCase.logEntityCreation(projectId)

        defaultTaskStatesTitleAndDescription.map {
            TaskState(
                id = UUID.randomUUID(), title = it[0], description = it[1],
                projectId = projectId,
            )
        }.forEach { taskStatesRepository.addNewTaskState(it, projectId) }
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