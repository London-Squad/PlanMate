package testDataHelpers.createInstanceHelper


import logic.entities.State
import java.time.LocalDateTime
import java.util.*


fun createState(
    id: UUID = UUID.randomUUID(),
    title: String = "Test State ${LocalDateTime.now()}",
    description: String = "Default state description"
): State = State(
    id = id,
    title = title,
    description = description
)