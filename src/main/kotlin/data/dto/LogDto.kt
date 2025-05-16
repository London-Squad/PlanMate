package data.dto

import org.bson.codecs.pojo.annotations.BsonId

data class LogCsvDto(
    val id: String,
    val userId: String,
    val time: String,
    val action: String,
    val planEntityId: String,
    val planEntityProperty: String,
    val oldValue: String,
    val newValue: String
)

data class LogMongoDto(
    @BsonId
    val id: String,
    val userId: String,
    val time: String,
    val action: String,
    val planEntityId: String,
    val planEntityProperty: String,
    val oldValue: String,
    val newValue: String
)