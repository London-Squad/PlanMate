package data.catchData

import logic.entities.Project
import logic.entities.State
import logic.entities.Task
import logic.entities.User
import logic.repositories.CatchDataRepository

class CatchDataMemoryRepository : CatchDataRepository {

    private var loggedInUser: User? = null
    private var selectedUser: User? = null
    private var selectedProject: Project? = null
    private var selectedState: State? = null
    private var selectedTask: Task? = null

    override fun getLoggedInUser(): User? = loggedInUser

    override fun setLoggedInUser(user: User) {
        loggedInUser = user
    }

    override fun clearLoggedInUserFromCatch() {
        loggedInUser = null
    }

    override fun getSelectedUser(): User? = selectedUser

    override fun setSelectedUser(user: User) {
        selectedUser = user
    }

    override fun clearSelectedUserFromCatch(){
        selectedUser = null
    }


    override fun getSelectedProject(): Project? = selectedProject

    override fun setSelectedProject(project: Project) {
        selectedProject = project
    }

    override fun clearSelectedProjectFromCatch() {
        selectedProject = null
    }


    override fun getSelectedState(): State? = selectedState

    override fun setSelectedState(state: State) {
        selectedState = state
    }

    override fun clearSelectedStateFromCatch() {
        selectedState = null
    }


    override fun getSelectedTask(): Task? = selectedTask

    override fun setSelectedTask(task: Task) {
        selectedTask = task
    }

    override fun clearSelectedTaskFromCatch() {
        selectedTask = null
    }

}