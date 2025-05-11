package data.repositories.dtoMappers

import data.dto.UserDto
import logic.entities.User

fun UserDto.toUser(): User {
    return User(
        id = id,
        userName = userName,
        type = User.Type.valueOf(type.uppercase())
    )
}

fun User.toUserDto(hashedPassword: String, isDeleted: Boolean = false): UserDto {
    return UserDto(
        id = id,
        userName = userName,
        hashedPassword = hashedPassword,
        type = type.name,
        isDeleted = isDeleted
    )
}