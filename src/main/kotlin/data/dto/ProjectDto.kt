package data.dto

import org.bson.codecs.pojo.annotations.BsonId

data class ProjectCsvDto(
    val id: String,
    val title: String,
    val description: String,
    val isDeleted: Boolean
)

data class ProjectMongoDto(
    @BsonId
    val id: String,
    val title: String,
    val description: String,
    val isDeleted: Boolean
)