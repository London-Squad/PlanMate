package data.repositories.dtoMappers.user

import data.dto.UserMongoDto

class UseMongoDtoMapper : UserDtoMapper<UserMongoDto> {

    override fun mapToUser(userDto: UserMongoDto) = userDto.toUser()

    override fun mapToCachedUser(userDto: UserMongoDto) = userDto.toUserDto()

}