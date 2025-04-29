package logic.entities

import java.util.UUID

interface PlanEntity {
    val id: UUID
    val title: String
    val description: String
}