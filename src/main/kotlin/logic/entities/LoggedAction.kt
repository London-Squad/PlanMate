package logic.entities

import java.util.UUID

sealed class LoggedAction

data class EntityCreationLog(val entityId: UUID) : LoggedAction()

data class EntityDeletionLog(val entityId: UUID) : LoggedAction()

data class EntityEditionLog(
    val entityId: UUID,
    val property: String,
    val oldValue: String,
    val newValue: String
) : LoggedAction()