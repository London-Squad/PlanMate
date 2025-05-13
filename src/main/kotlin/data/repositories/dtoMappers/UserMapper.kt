package data.repositories.dtoMappers

import data.dto.UserDto
import logic.entities.User
import java.util.*

fun UserDto.toUser(): User {
    return User(
        id = UUID.fromString(id),
        userName = userName,
        type = User.Type.valueOf(type.uppercase())
    )
}