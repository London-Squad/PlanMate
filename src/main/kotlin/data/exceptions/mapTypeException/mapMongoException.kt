package data.exceptions.mapTypeException

import com.mongodb.MongoException
import data.exceptions.DataAccessException
import data.exceptions.DataOperationException
import data.exceptions.DataRetrievalFailureException
import data.exceptions.*
import data.exceptions.sharedOperationTypes.GeneralOperationType
import data.exceptions.sharedOperationTypes.MongoOperationName

fun mapMongoException(operation: MongoOperationName, e: MongoException): DataAccessException {
    val operationType = determineMongoOperationType(operation)
    val message = "Failed to ${operation.name.lowercase()}: ${e.message}"
    return when (operationType) {
        GeneralOperationType.RETRIEVAL -> DataRetrievalFailureException(message)
        GeneralOperationType.MODIFICATION -> DataOperationException(message)
        GeneralOperationType.CONNECTION -> DataConnectionException(message)
        GeneralOperationType.PARSING -> DataParsingException(message)
        GeneralOperationType.CLOSING -> DataOperationException(message)
    }
}

private fun determineMongoOperationType(operation: MongoOperationName): GeneralOperationType = when (operation) {
    MongoOperationName.RETRIEVE_PROJECTS -> GeneralOperationType.RETRIEVAL
    MongoOperationName.INSERT_PROJECT,
    MongoOperationName.UPDATE_PROJECT_TITLE,
    MongoOperationName.UPDATE_PROJECT_DESCRIPTION,
    MongoOperationName.DELETE_PROJECT,
    MongoOperationName.DELETE_USER,
    MongoOperationName.DELETE_TASK,
    MongoOperationName.DELETE_TASK_STATE -> GeneralOperationType.MODIFICATION
    MongoOperationName.CONNECT_TO_MONGO -> GeneralOperationType.CONNECTION
    MongoOperationName.CLOSE_MONGO_CONNECTION -> GeneralOperationType.CLOSING
}