package data.dto

import org.bson.codecs.pojo.annotations.BsonId

data class TaskStateCsvDto(
    val id: String,
    val title: String,
    val description: String,
    val projectId: String,
    val isDeleted: Boolean
)

data class TaskStateMongoDto(
    @BsonId
    val id: String,
    val title: String,
    val description: String,
    val projectId: String,
    val isDeleted: Boolean
)