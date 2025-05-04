package logic.useCases

import logic.entities.*
import logic.repositories.LogsRepository
import logic.repositories.TaskRepository
import java.util.UUID

class ManageTaskUseCase(
    private val taskRepository: TaskRepository,
    private val logsRepository: LogsRepository,
) {

    fun editTaskTitle(taskID: UUID, newTitle: String) {
        val task = taskRepository.getTaskByID(taskID)!!

        taskRepository.editTaskTitle(taskID, newTitle)

        logsRepository.addEditionLog(
            task,
            "title",
            task.title,
            newTitle
        )
    }

    fun editTaskDescription(taskID: UUID, newDescription: String) {

        val task = taskRepository.getTaskByID(taskID)!!

        taskRepository.editTaskDescription(taskID, newDescription)

        logsRepository.addEditionLog(
            task,
            "description",
            task.description,
            newDescription
        )
    }

    fun editTaskState(taskID: UUID, newState: State) {
        val task = taskRepository.getTaskByID(taskID)!!

        taskRepository.editTaskState(taskID, newState)

        logsRepository.addEditionLog(
            task,
            "state",
            task.state.title,
            newState.title
        )
    }

    fun deleteTask(taskID: UUID) {

        val task = taskRepository.getTaskByID(taskID)!!

        logsRepository.addDeletionLog(task)

        taskRepository.deleteTask(taskID)
    }

}