package logic.useCases

import logic.entities.*
import logic.repositories.ProjectsRepository
import logic.repositories.TaskRepository
import java.util.*

class ManageTaskUseCase(
    private val taskRepository: TaskRepository,
    private val createLogUseCase: CreateLogUseCase,
    private val projectsRepository: ProjectsRepository
) {

    suspend fun getTaskByID(taskId: UUID): Task{
        return taskRepository.getTaskByID(taskId)
    }

    suspend fun addNewTask(title: String, description: String, projectId: UUID) {

        val newTask = buildNewTask(UUID.randomUUID(), title, description, projectId)

        taskRepository.addNewTask(newTask, projectId)
        createLogUseCase.logEntityCreation(newTask)
    }

    private suspend fun buildNewTask(
        id: UUID,
        title: String,
        description: String,
        projectId: UUID
    ): Task {
        return Task(
            id = id,
            title = title,
            description = description,
            taskState = projectsRepository.getProjectById(projectId).tasksStates.first()
        )
    }

    suspend fun editTaskTitle(taskId: UUID, newTitle: String) {
        val oldTask = taskRepository.getTaskByID(taskId)

        taskRepository.editTaskTitle(taskId, newTitle)
        createLogUseCase.logEntityTitleEdition(oldTask, oldTask.title, newTitle)
    }

    suspend fun editTaskDescription(taskId: UUID, newDescription: String) {
        val oldTask = taskRepository.getTaskByID(taskId)

        taskRepository.editTaskDescription(taskId, newDescription)
        createLogUseCase.logEntityDescriptionEdition(oldTask, oldTask.description, newDescription)
    }

    suspend fun editTaskState(taskId: UUID, newTaskState: TaskState) {
        val oldTask = taskRepository.getTaskByID(taskId)

        taskRepository.editTaskState(taskId, newTaskState)
        createLogUseCase.logTaskStateEdition(oldTask, oldTask.taskState.title, newTaskState.title)
    }

    suspend fun deleteTask(taskID: UUID) {
        val task = taskRepository.getTaskByID(taskID)

        taskRepository.deleteTask(taskID)
        createLogUseCase.logEntityDeletion(task)
    }
}