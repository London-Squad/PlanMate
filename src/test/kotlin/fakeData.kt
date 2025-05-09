import logic.entities.User
import java.util.*

object fakeData {
    val mate = User(
        id = UUID.randomUUID(),
        userName = "fakeMate",
        type = User.Type.MATE
    )

    val admin = User(
        id = UUID.randomUUID(),
        userName = "fakeMate",
        type = User.Type.MATE
    )

    val users = listOf(
        mate,
        admin
    )
}