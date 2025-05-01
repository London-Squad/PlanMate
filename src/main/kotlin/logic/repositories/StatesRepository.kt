package logic.repositories

import logic.entities.State
import java.util.UUID

interface StatesRepository {

    fun getAllStatesByProjectId(projectId: UUID): List<State>

    fun getStateById(stateId: UUID): State

    fun addNewState(state: State, projectId: UUID)

    fun editStateTitle(stateId: UUID, newTitle: String)

    fun editStateDescription(stateId: UUID, newDescription: String)

    fun deleteState(stateId: UUID)

    fun getStateById(stateId: UUID): State?
    fun getStatesByProjectId(projectId: UUID): List<State>
}