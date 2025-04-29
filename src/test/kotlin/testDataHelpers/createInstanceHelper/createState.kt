package testDataHelpers.createInstanceHelper

import logic.entities.State
import java.time.LocalDateTime
import java.util.UUID

fun createState(
    id: UUID = UUID.randomUUID(),
    title: String = "Test State ${LocalDateTime.now()}",
    description: String = "Default state description"
): State = State(
    id = id,
    title = title,
    description = description
)