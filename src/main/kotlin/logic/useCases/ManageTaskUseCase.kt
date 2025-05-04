package logic.useCases

import logic.entities.Delete
import logic.entities.Edit
import logic.entities.Log
import logic.entities.State
import logic.repositories.AuthenticationRepository
import logic.repositories.LogsRepository
import logic.repositories.TaskRepository
import java.util.*

class ManageTaskUseCase(
    private val taskRepository: TaskRepository,
    private val authenticationRepository: AuthenticationRepository,
    private val logsRepository: LogsRepository,
) {

    fun editTaskTitle(taskID: UUID, newTitle: String) {

        logsRepository.addLog(
            Log(
                user = authenticationRepository.getLoggedInUser(),
                action = Edit(
                    entity = taskRepository.getTaskByID(taskID)!!,
                    property = "title",
                    oldValue = taskRepository.getTaskByID(taskID)!!.title,
                    newValue = newTitle
                )
            )
        )

        taskRepository.editTaskTitle(taskID, newTitle)
    }

    fun editTaskDescription(taskID: UUID, newDescription: String) {

        logsRepository.addLog(
            Log(
                user = authenticationRepository.getLoggedInUser(),
                action = Edit(
                    entity = taskRepository.getTaskByID(taskID)!!,
                    property = "description",
                    oldValue = taskRepository.getTaskByID(taskID)!!.description,
                    newValue = newDescription
                )
            )
        )

        taskRepository.editTaskDescription(taskID, newDescription)
    }

    fun editTaskState(taskID: UUID, newState: State) {

        logsRepository.addLog(
            Log(
                user = authenticationRepository.getLoggedInUser(),
                action = Edit(
                    entity = taskRepository.getTaskByID(taskID)!!,
                    property = "state",
                    oldValue = taskRepository.getTaskByID(taskID)!!.state.title,
                    newValue = newState.title
                )
            )
        )

        taskRepository.editTaskState(taskID, newState)
    }

    fun deleteTask(taskID: UUID) {

        logsRepository.addLog(
            Log(
                user = authenticationRepository.getLoggedInUser(),
                action = Delete(
                    entity = taskRepository.getTaskByID(taskID)!!,
                )
            )
        )

        taskRepository.deleteTask(taskID)
    }

}