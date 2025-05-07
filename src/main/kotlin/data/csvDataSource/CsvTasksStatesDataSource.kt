package data.csvDataSource

import data.csvDataSource.fileIO.CsvFileHandler
import data.csvDataSource.fileIO.CsvParser
import data.dataSources.TasksStatesDataSource
import data.dto.TaskStateDto
import java.util.UUID

class CsvTasksStatesDataSource(
    private val tasksStatesCsvFileHandler: CsvFileHandler,
    private val csvParser: CsvParser
) : TasksStatesDataSource {

    override fun getAllTasksStates(): List<TaskStateDto> {
        return tasksStatesCsvFileHandler.readRecords()
            .map(csvParser::recordToTaskStateDto)
    }

    override fun getDefaultTasksStates(projectId: UUID): List<TaskStateDto> {
        return listOf(
            TaskStateDto(
                id = UUID.randomUUID(), title = "TODO", description = "TO DO TASKS",
                projectId = projectId,
                isDeleted = false
            ),
            TaskStateDto(
                id = UUID.randomUUID(), title = "InProgress", description = "INPROGRESS TASKS",
                projectId = projectId,
                isDeleted = false
            ),
            TaskStateDto(
                id = UUID.randomUUID(), title = "Done", description = "FINISHED TASKS",
                projectId = projectId,
                isDeleted = false
            )
        )
    }

    override fun addNewTaskState(taskStateDto: TaskStateDto) {
        tasksStatesCsvFileHandler.appendRecord(
            csvParser.taskStateDtoToRecord(taskStateDto)
        )
    }

    override fun editTaskStateTitle(stateId: UUID, newTitle: String) {
        tasksStatesCsvFileHandler.readRecords()
            .map {
                val newTaskData = csvParser.recordToTaskStateDto(it)
                if (newTaskData.id == stateId) {
                    csvParser.taskStateDtoToRecord(newTaskData.copy(title = newTitle))
                } else it
            }
            .also(tasksStatesCsvFileHandler::rewriteRecords)
    }

    override fun editTaskStateDescription(stateId: UUID, newDescription: String) {
        tasksStatesCsvFileHandler.readRecords()
            .map {
                val newTaskData = csvParser.recordToTaskStateDto(it)
                if (newTaskData.id == stateId) {
                    csvParser.taskStateDtoToRecord(newTaskData.copy(description = newDescription))
                } else it
            }
            .also(tasksStatesCsvFileHandler::rewriteRecords)
    }

    override fun deleteTaskState(stateId: UUID) {
        tasksStatesCsvFileHandler.readRecords()
            .map {
                val newTaskData = csvParser.recordToTaskStateDto(it)
                if (newTaskData.id == stateId) {
                    csvParser.taskStateDtoToRecord(newTaskData.copy(isDeleted = true))
                } else it
            }
            .also(tasksStatesCsvFileHandler::rewriteRecords)
    }
}