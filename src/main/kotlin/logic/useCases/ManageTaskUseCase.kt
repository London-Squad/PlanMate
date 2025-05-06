package logic.useCases

import logic.entities.*
import logic.repositories.AuthenticationRepository
import logic.repositories.LogsRepository
import logic.repositories.ProjectsRepository
import logic.repositories.TaskRepository
import java.util.*

class ManageTaskUseCase(
    private val taskRepository: TaskRepository,
    private val authenticationRepository: AuthenticationRepository,
    private val logsRepository: LogsRepository,
    private val projectsRepository: ProjectsRepository
) {

    fun createNewTask(title: String, description: String, projectId: UUID) {

        val newTask = buildNewTask(title, description, projectId)

        taskRepository.addNewTask(newTask, projectId)

        logsRepository.addLog(
            Log(
                user = authenticationRepository.getLoggedInUser(),
                action = Create(newTask)
            )
        )
    }

    private fun buildNewTask(
        title: String,
        description: String,
        projectId: UUID
    ): Task {
        return Task(
            title = title,
            description = description,
            taskState = projectsRepository.getProjectById(projectId).tasksStates.first()
        )
    }

    fun editTaskTitle(taskId: UUID, newTitle: String) {
        val task = taskRepository.getTaskByID(taskId)

        taskRepository.editTaskTitle(taskId, newTitle)
        logsRepository.addLog(
            Log(
                user = authenticationRepository.getLoggedInUser(),
                action = Edit(
                    entity = task,
                    property = "title",
                    oldValue = task.title,
                    newValue = newTitle
                )
            )
        )
    }

    fun editTaskDescription(taskId: UUID, newDescription: String) {
        val task = taskRepository.getTaskByID(taskId)

        taskRepository.editTaskDescription(taskId, newDescription)
        logsRepository.addLog(
            Log(
                user = authenticationRepository.getLoggedInUser(),
                action = Edit(
                    entity = task,
                    property = "description",
                    oldValue = task.description,
                    newValue = newDescription
                )
            )
        )
    }

    fun editTaskState(taskId: UUID, newTaskState: TaskState) {
        val task = taskRepository.getTaskByID(taskId)

        taskRepository.editTaskState(taskId, newTaskState)
        logsRepository.addLog(
            Log(
                user = authenticationRepository.getLoggedInUser(),
                action = Edit(
                    entity = task,
                    property = "state",
                    oldValue = task.taskState.title,
                    newValue = newTaskState.title
                )
            )
        )
    }

    fun deleteTask(taskID: UUID) {
        val task = taskRepository.getTaskByID(taskID)

        taskRepository.deleteTask(taskID)

        logsRepository.addLog(
            Log(
                user = authenticationRepository.getLoggedInUser(),
                action = Delete(
                    entity = task,
                )
            )
        )
    }

}