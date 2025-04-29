package logic.repositories

import logic.entities.Project
import logic.entities.State
import logic.entities.Task
import logic.entities.User

interface CatchDataRepository {

    fun getLoggedInUser(): User?

    fun setLoggedInUser(user: User)

    fun clearLoggedInUserFromCatch()


    fun getSelectedProject(): Project?

    fun setSelectedProject(project: Project)

    fun clearSelectedProjectFromCatch()


    fun getSelectedState(): State?

    fun setSelectedState(state: State)

    fun clearSelectedStateFromCatch()


    fun getSelectedTask(): Task?

    fun setSelectedTask(task: Task)

    fun clearSelectedTaskFromCatch()

}