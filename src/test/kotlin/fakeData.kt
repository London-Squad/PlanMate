
import logic.entities.*
import java.time.LocalDateTime
import java.util.*

object fakeData {
    val mate = User(
        id = UUID.fromString("423e4567-e89b-12d3-a456-426614174003"),
        userName = "fakeMate",
        type = User.Type.MATE
    )

    val admin = User(
        id = UUID.fromString("523e4567-e89b-12d3-a456-426614174004"),
        userName = "fakeAdmin",
        type = User.Type.ADMIN
    )

    val users = listOf(mate, admin)

    val fakeProject = Project(
        id = UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
        title = "Fake Project",
        description = "This is a fake project for testing purposes"
    )

    val fakeTaskState = TaskState(
        id = UUID.fromString("323e4567-e89b-12d3-a456-426614174002"),
        title = "Fake Task State",
        description = "This is a fake task state for testing purposes",
        projectId = fakeProject.id
    )

    val fakeTask = Task(
        id = UUID.fromString("223e4567-e89b-12d3-a456-426614174001"),
        title = "Fake Task",
        description = "This is a fake task for testing purposes",
        taskStateId = fakeTaskState.id
    )

    val fakeLog = Log(
        id = UUID.fromString("623e4567-e89b-12d3-a456-426614174005"),
        userId = mate.id,
        time = LocalDateTime.of(2025, 5, 16, 7, 22, 0),
        loggedAction = EntityCreationLog(entityId = fakeProject.id),
        entityType = Log.EntityType.PROJECT
    )
}