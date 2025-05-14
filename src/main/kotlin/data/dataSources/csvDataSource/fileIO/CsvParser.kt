package data.dataSources.csvDataSource.fileIO

import data.dto.*
import logic.exceptions.RetrievingDataFailureException

class CsvParser {
    fun taskDtoToRecord(taskDto: TaskDto): List<String> {
        return listOf(
            taskDto.id,
            taskDto.title,
            taskDto.description,
            taskDto.stateId,
            taskDto.projectId,
            taskDto.isDeleted.toString()
        )
    }

    fun recordToTaskDto(record: List<String>): TaskDto {
        if (record.size != 6) throw RetrievingDataFailureException("Invalid TaskData record size")
        return TaskDto(
            id = record[0],
            title = record[1],
            description = record[2],
            stateId = record[3],
            projectId = record[4],
            isDeleted = record[5].toBoolean()
        )
    }

    fun taskStateDtoToRecord(stateData: TaskStateDto): List<String> {
        return listOf(
            stateData.id,
            stateData.title,
            stateData.description,
            stateData.projectId,
            stateData.isDeleted.toString()
        )
    }

    fun recordToTaskStateDto(record: List<String>): TaskStateDto {
        if (record.size != 5) throw RetrievingDataFailureException("Invalid StateData record size")
        return TaskStateDto(
            id = record[0],
            title = record[1],
            description = record[2],
            projectId = record[3],
            isDeleted = record[4].toBoolean()
        )
    }

    fun projectDtoToRecord(projectDto: ProjectDto): List<String> {
        return listOf(
            projectDto.id,
            projectDto.title,
            projectDto.description,
            projectDto.isDeleted.toString()
        )
    }

    fun recordToProjectDto(record: List<String>): ProjectDto {
        if (record.size != 4) throw RetrievingDataFailureException("Invalid ProjectData record size")
        return ProjectDto(
            id = record[0],
            title = record[1],
            description = record[2],
            isDeleted = record[3].toBoolean()
        )
    }

    fun userDtoToRecord(userDto: UserDto): List<String> {
        return listOf(
            userDto.id,
            userDto.userName,
            userDto.type,
            userDto.hashedPassword,
            userDto.isDeleted.toString()
        )
    }

    fun recordToUserDto(record: List<String>): UserDto {
        if (record.size != 5) throw RetrievingDataFailureException("Invalid UserData record size")
        return UserDto(
            id = record[0],
            userName = record[1],
            type = record[2],
            hashedPassword = record[3],
            isDeleted = record[4].toBoolean()
        )
    }

    fun logDtoToRecord(logDto: LogDto): List<String> {
        return listOf(
            logDto.id,
            logDto.userId,
            logDto.time,
            logDto.action,
            logDto.planEntityId,
            logDto.planEntityProperty,
            logDto.oldValue,
            logDto.newValue
        )
    }

    fun recordToLogDto(record: List<String>): LogDto {
        if (record.size != 9) throw RetrievingDataFailureException("Invalid LogData record size")
        return LogDto(
            id = record[0],
            userId = record[1],
            time = record[2],
            action = record[3],
            planEntityId = record[4],
            planEntityProperty = record[5],
            oldValue = record[6],
            newValue = record[7],
            entityType = record[8]
        )
    }
}