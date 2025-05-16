package data.dto

import org.bson.codecs.pojo.annotations.BsonId

data class TaskCsvDto(
    val id: String,
    val title: String,
    val description: String,
    val stateId: String,
    val projectId: String,
    val isDeleted: Boolean
)

data class TaskMongoDto(
    @BsonId
    val id: String,
    val title: String,
    val description: String,
    val stateId: String,
    val projectId: String,
    val isDeleted: Boolean
)