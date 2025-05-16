package data.repositories.dtoMappers.user

import data.dto.UserCsvDto
import data.dto.UserMongoDto
import logic.entities.User
import java.util.*

fun UserCsvDto.toUser(): User {
    return User(
        id = UUID.fromString(id),
        userName = userName,
        type = User.Type.valueOf(type.uppercase())
    )
}

fun UserMongoDto.toUser(): User {
    return User(
        id = UUID.fromString(id),
        userName = userName,
        type = User.Type.valueOf(type.uppercase())
    )
}

fun UserMongoDto.toUserDto(): UserCsvDto {
    return UserCsvDto(
        id = id,
        userName = userName,
        type = type,
        hashedPassword = hashedPassword,
        isDeleted = isDeleted
    )
}