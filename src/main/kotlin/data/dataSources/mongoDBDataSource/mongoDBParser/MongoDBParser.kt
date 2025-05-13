package data.dataSources.mongoDBDataSource.mongoDBParser

import data.dto.*
import logic.exceptions.RetrievingDataFailureException
import org.bson.Document
import java.time.LocalDateTime
import java.util.*

class MongoDBParser {

    fun taskDtoToDocument(taskDto: TaskDto): Document {
        return Document(ID_FIELD, taskDto.id.toString())
            .append(TITLE_FIELD, taskDto.title)
            .append(DESCRIPTION_FIELD, taskDto.description)
            .append(STATE_ID_FIELD, taskDto.stateId.toString())
            .append(PROJECT_ID_FIELD, taskDto.projectId.toString())
            .append(IS_DELETED_FIELD, taskDto.isDeleted)
    }

    fun documentToTaskDto(document: Document): TaskDto {
        if (document.size != 7) throw RetrievingDataFailureException("Invalid TaskData document size")
        return TaskDto(
            id = UUID.fromString(document.getString(ID_FIELD)),
            title = document.getString(TITLE_FIELD),
            description = document.getString(DESCRIPTION_FIELD),
            stateId = UUID.fromString(document.getString(STATE_ID_FIELD)),
            projectId = UUID.fromString(document.getString(PROJECT_ID_FIELD)),
            isDeleted = document.getBoolean(IS_DELETED_FIELD) ?: false
        )
    }

    fun taskStateDtoToDocument(stateDto: TaskStateDto): Document {
        return Document(ID_FIELD, stateDto.id.toString())
            .append(TITLE_FIELD, stateDto.title)
            .append(DESCRIPTION_FIELD, stateDto.description)
            .append(PROJECT_ID_FIELD, stateDto.projectId.toString())
            .append(IS_DELETED_FIELD, stateDto.isDeleted)
    }

    fun documentToTaskStateDto(document: Document): TaskStateDto {
        if (document.size != 6) throw RetrievingDataFailureException("Invalid TaskData document size")
        return TaskStateDto(
            id = UUID.fromString(document.getString(ID_FIELD)),
            title = document.getString(TITLE_FIELD),
            description = document.getString(DESCRIPTION_FIELD),
            projectId = UUID.fromString(document.getString(PROJECT_ID_FIELD)),
            isDeleted = document.getBoolean(IS_DELETED_FIELD) ?: false
        )
    }

    fun projectDtoToDocument(projectDto: ProjectDto): Document {
        return Document(ID_FIELD, projectDto.id.toString())
            .append(TITLE_FIELD, projectDto.title)
            .append(DESCRIPTION_FIELD, projectDto.description)
            .append(IS_DELETED_FIELD, projectDto.isDeleted)
    }

    fun documentToProjectDto(document: Document): ProjectDto {
        if (document.size != 5) throw RetrievingDataFailureException("Invalid TaskData document size")
        return ProjectDto(
            id = UUID.fromString(document.getString(ID_FIELD)),
            title = document.getString(TITLE_FIELD),
            description = document.getString(DESCRIPTION_FIELD),
            isDeleted = document.getBoolean(IS_DELETED_FIELD) ?: false
        )
    }

    fun userDtoToDocument(userDto: UserDto): Document {
        return Document(ID_FIELD, UUID.randomUUID().toString())
            .append(USERNAME_FIELD, userDto.userName)
            .append(PASSWORD_FIELD, userDto.hashedPassword)
            .append(TYPE_FIELD, userDto.type)
            .append(IS_DELETED_FIELD, userDto.isDeleted)
    }

    fun documentToUserDto(document: Document): UserDto {
        if (document.size != 6) throw RetrievingDataFailureException("Invalid TaskData document size")
        return UserDto(
            id = UUID.fromString(document.getString(ID_FIELD)),
            userName = document.getString(USERNAME_FIELD),
            hashedPassword = document.getString(PASSWORD_FIELD),
            type = document.getString(TYPE_FIELD),
            isDeleted = document.getBoolean(IS_DELETED_FIELD) ?: false
        )
    }

    fun logDtoToDocument(logDto: LogDto): Document {
        return Document(ID_FIELD, logDto.id.toString())
            .append(USER_ID_FIELD, logDto.userId.toString())
            .append(TIME_FIELD, logDto.time.toString())
            .append(ACTION_FIELD, logDto.action)
            .append(PLAN_ENTITY_ID_FIELD, logDto.planEntityId.toString())
            .append(PLAN_ENTITY_PROPERTY_FIELD, logDto.planEntityProperty)
            .append(PLAN_OLD_VALUE_FIELD, logDto.oldValue)
            .append(PLAN_NEW_VALUE_FIELD, logDto.newValue)
    }

    fun documentToLogDto(document: Document): LogDto {
        if (document.size != 9) throw RetrievingDataFailureException("Invalid TaskData document size")
        return LogDto(
            id = UUID.fromString(document.getString(ID_FIELD)),
            userId = UUID.fromString(document.getString(USER_ID_FIELD)),
            time = LocalDateTime.parse(document.getString(TIME_FIELD)),
            action = document.getString(ACTION_FIELD),
            planEntityId = UUID.fromString(document.getString(PLAN_ENTITY_ID_FIELD)),
            planEntityProperty = document.getString(PLAN_ENTITY_PROPERTY_FIELD),
            oldValue = document.getString(PLAN_OLD_VALUE_FIELD),
            newValue = document.getString(PLAN_NEW_VALUE_FIELD)
        )
    }

    companion object {
        const val ID_FIELD = "id"
        const val TITLE_FIELD = "title"
        const val DESCRIPTION_FIELD = "description"
        const val STATE_ID_FIELD = "stateId"
        const val PROJECT_ID_FIELD = "projectId"
        const val IS_DELETED_FIELD = "isDeleted"
        const val USERNAME_FIELD = "userName"
        const val PASSWORD_FIELD = "hashedPassword"
        const val TYPE_FIELD = "type"
        const val USER_ID_FIELD = "userId"
        const val TIME_FIELD = "time"
        const val ACTION_FIELD = "action"
        const val PLAN_ENTITY_ID_FIELD = "planEntityId"
        const val PLAN_ENTITY_PROPERTY_FIELD = "planEntityProperty"
        const val PLAN_OLD_VALUE_FIELD = "oldValue"
        const val PLAN_NEW_VALUE_FIELD = "newValue"
    }
}