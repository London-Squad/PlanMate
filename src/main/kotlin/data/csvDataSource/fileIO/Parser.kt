package data.csvDataSource.fileIO

import data.entitiesData.*
import logic.exceptions.RetrievingDataFailureException
import java.time.LocalDateTime
import java.util.*

class Parser {
    fun taskDataToRecord(taskData: TaskData): List<String> {
        return listOf(
            taskData.id.toString(),
            taskData.title,
            taskData.description,
            taskData.stateId.toString(),
            taskData.projectId.toString(),
            taskData.isDeleted.toString()
        )
    }

    fun recordToTaskData(record: List<String>): TaskData {
        if (record.size != 6) throw RetrievingDataFailureException("Invalid TaskData record size")
        return TaskData(
            id = UUID.fromString(record[0]),
            title = record[1],
            description = record[2],
            stateId = UUID.fromString(record[3]),
            projectId = UUID.fromString(record[4]),
            isDeleted = record[5].toBoolean()
        )
    }

    fun taskStateDataToRecord(stateData: TaskStateData): List<String> {
        return listOf(
            stateData.id.toString(),
            stateData.title,
            stateData.description,
            stateData.projectId.toString(),
            stateData.isDeleted.toString()
        )
    }

    fun recordToTaskStateData(record: List<String>): TaskStateData {
        if (record.size != 5) throw RetrievingDataFailureException("Invalid StateData record size")
        return TaskStateData(
            id = UUID.fromString(record[0]),
            title = record[1],
            description = record[2],
            projectId = UUID.fromString(record[3]),
            isDeleted = record[4].toBoolean()
        )
    }

    fun projectDataToRecord(projectData: ProjectData): List<String> {
        return listOf(
            projectData.id.toString(),
            projectData.title,
            projectData.description,
            projectData.isDeleted.toString()
        )
    }

    fun recordToProjectData(record: List<String>): ProjectData {
        if (record.size != 4) throw RetrievingDataFailureException("Invalid ProjectData record size")
        return ProjectData(
            id = UUID.fromString(record[0]),
            title = record[1],
            description = record[2],
            isDeleted = record[3].toBoolean()
        )
    }

    fun userDataToRecord(userData: UserData): List<String> {
        return listOf(
            userData.id.toString(),
            userData.userName,
            userData.isDeleted.toString()
        )
    }

    fun recordToUserData(record: List<String>): UserData {
        if (record.size != 4) throw RetrievingDataFailureException("Invalid UserData record size")
        return UserData(
            id = UUID.fromString(record[0]),
            userName = record[1],
            type = record[2],
            hashedPassword = record[3],
            isDeleted = record[4].toBoolean()
        )
    }

    fun logDataToRecord(logData: LogData): List<String> {
        return listOf(
            logData.id.toString(),
            logData.userId.toString(),
            logData.time.toString(),
            logData.action,
            logData.planEntityId.toString(),
            logData.planEntityProperty,
            logData.oldValue,
            logData.newValue
        )
    }

    fun recordToLogData(record: List<String>): LogData {
        if (record.size != 8) throw RetrievingDataFailureException("Invalid LogData record size")
        return LogData(
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