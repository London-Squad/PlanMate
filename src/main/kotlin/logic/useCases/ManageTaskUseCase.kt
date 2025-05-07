package logic.useCases

import logic.entities.*
import logic.repositories.ProjectsRepository
import logic.repositories.TaskRepository
import java.util.*

class ManageTaskUseCase(
    private val taskRepository: TaskRepository,
    private val addLogUseCase: AddLogUseCase,
    private val projectsRepository: ProjectsRepository
) {

    fun addNewTask(title: String, description: String, projectId: UUID) {

        val newTask = buildNewTask(UUID.randomUUID(), title, description, projectId)

        taskRepository.addNewTask(newTask, projectId)
        addLogUseCase.logEntityCreation(newTask)
    }

    private fun buildNewTask(
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

    fun editTaskTitle(taskId: UUID, newTitle: String) {
        val oldTask = taskRepository.getTaskByID(taskId)

        taskRepository.editTaskTitle(taskId, newTitle)
        addLogUseCase.logEntityTitleEdition(oldTask, oldTask.title, newTitle)
    }

    fun editTaskDescription(taskId: UUID, newDescription: String) {
        val oldTask = taskRepository.getTaskByID(taskId)

        taskRepository.editTaskDescription(taskId, newDescription)
        addLogUseCase.logEntityDescriptionEdition(oldTask, oldTask.description, newDescription)
    }

    fun editTaskState(taskId: UUID, newTaskState: TaskState) {
        val oldTask = taskRepository.getTaskByID(taskId)

        taskRepository.editTaskState(taskId, newTaskState)
        addLogUseCase.logTaskStateEdition(oldTask, oldTask.taskState.title, newTaskState.title)
    }

    fun deleteTask(taskID: UUID) {
        val task = taskRepository.getTaskByID(taskID)

        taskRepository.deleteTask(taskID)
        addLogUseCase.logEntityDeletion(task)
    }
}