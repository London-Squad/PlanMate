package data.dto

import org.bson.codecs.pojo.annotations.BsonId

data class UserCsvDto(
    val id: String,
    val userName: String,
    val hashedPassword: String,
    val type: String,
    val isDeleted: Boolean
)

data class UserMongoDto(
    @BsonId
    val id: String,
    val userName: String,
    val hashedPassword: String,
    val type: String,
    val isDeleted: Boolean
)