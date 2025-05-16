import logic.entities.User
import java.util.UUID

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

}