package data.dataSources.csvDataSource.fileIO

import data.dto.*
import logic.exceptions.RetrievingDataFailureException
import java.time.LocalDateTime
import java.util.*

class CsvParser {
    fun taskDtoToRecord(taskDto: TaskDto): List<String> {
        return listOf(
            taskDto.id.toString(),
            taskDto.title,
            taskDto.description,
            taskDto.stateId.toString(),
            taskDto.projectId.toString(),
            taskDto.isDeleted.toString()
        )
    }

    fun recordToTaskDto(record: List<String>): TaskDto {
        if (record.size != 6) throw RetrievingDataFailureException("Invalid TaskData record size")
        return TaskDto(
            id = UUID.fromString(record[0]),
            title = record[1],
            description = record[2],
            stateId = UUID.fromString(record[3]),
            projectId = UUID.fromString(record[4]),
            isDeleted = record[5].toBoolean()
        )
    }

    fun taskStateDtoToRecord(stateData: TaskStateDto): List<String> {
        return listOf(
            stateData.id.toString(),
            stateData.title,
            stateData.description,
            stateData.projectId.toString(),
            stateData.isDeleted.toString()
        )
    }

    fun recordToTaskStateDto(record: List<String>): TaskStateDto {
        if (record.size != 5) throw RetrievingDataFailureException("Invalid StateData record size")
        return TaskStateDto(
            id = UUID.fromString(record[0]),
            title = record[1],
            description = record[2],
            projectId = UUID.fromString(record[3]),
            isDeleted = record[4].toBoolean()
        )
    }

    fun projectDtoToRecord(projectDto: ProjectDto): List<String> {
        return listOf(
            projectDto.id.toString(),
            projectDto.title,
            projectDto.description,
            projectDto.isDeleted.toString()
        )
    }

    fun recordToProjectDto(record: List<String>): ProjectDto {
        if (record.size != 4) throw RetrievingDataFailureException("Invalid ProjectData record size")
        return ProjectDto(
            id = UUID.fromString(record[0]),
            title = record[1],
            description = record[2],
            isDeleted = record[3].toBoolean()
        )
    }

    fun userDtoToRecord(userDto: UserDto): List<String> {
        return listOf(
            userDto.id.toString(),
            userDto.userName,
            userDto.type,
            userDto.hashedPassword,
            userDto.isDeleted.toString()
        )
    }

    fun recordToUserDto(record: List<String>): UserDto {
        if (record.size != 5) throw RetrievingDataFailureException("Invalid UserData record size")
        return UserDto(
            id = UUID.fromString(record[0]),
            userName = record[1],
            type = record[2],
            hashedPassword = record[3],
            isDeleted = record[4].toBoolean()
        )
    }

    fun logDtoToRecord(logDto: LogDto): List<String> {
        return listOf(
            logDto.id.toString(),
            logDto.userId.toString(),
            logDto.time.toString(),
            logDto.action,
            logDto.planEntityId.toString(),
            logDto.planEntityProperty,
            logDto.oldValue,
            logDto.newValue
        )
    }

    fun recordToLogDto(record: List<String>): LogDto {
        if (record.size != 8) throw RetrievingDataFailureException("Invalid LogData record size")
        return LogDto(
            id = UUID.fromString(record[0]),
            userId = UUID.fromString(record[1]),
            time = LocalDateTime.parse(record[2]),
            action = record[3],
            planEntityId = UUID.fromString(record[4]),
            planEntityProperty = record[5],
            oldValue = record[6],
            newValue = record[7]
        )
    }
}