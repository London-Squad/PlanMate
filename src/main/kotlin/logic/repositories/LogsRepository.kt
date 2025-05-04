package logic.repositories

import logic.entities.Log
import logic.entities.PlanEntity
import java.util.UUID

interface LogsRepository {

    fun getAllLogs(): List<Log>

    fun getLogsByEntityId(entityId: UUID): List<Log>

    fun addCreationLog(
        planEntity: PlanEntity,
    )

    fun addDeletionLog(
        planEntity: PlanEntity,
    )

    fun addEditionLog(
        planEntity: PlanEntity,
        planEntityPropertyToChange: String,
        oldValue: String,
        newValue: String
    )

}