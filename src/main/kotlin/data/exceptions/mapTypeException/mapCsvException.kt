package data.exceptions.mapTypeException

import data.exceptions.CSVFileAccessException
import data.exceptions.CSVParsingException
import data.exceptions.CSVReadFailureException
import data.exceptions.CSVWriteFailureException
import data.exceptions.DataAccessException
import data.exceptions.DataConnectionException
import data.exceptions.DataOperationException
import data.exceptions.DataParsingException
import data.exceptions.DataRetrievalFailureException
import data.exceptions.sharedOperationTypes.CSVOperationName
import data.exceptions.sharedOperationTypes.GeneralOperationType
import java.io.IOException


fun mapCsvException(operation: CSVOperationName, e: IOException): DataAccessException {
    val operationType = determineCSVOperationType(operation)
    val message = "Failed to ${operation.name.lowercase()}: ${e.message}"
    return when (operationType) {
        GeneralOperationType.RETRIEVAL -> {
            when (operation) {
                CSVOperationName.READ_CSV_DATA -> CSVReadFailureException(message)
                else -> DataRetrievalFailureException(message)
            }
        }
        GeneralOperationType.MODIFICATION -> {
            when (operation) {
                CSVOperationName.WRITE_CSV_DATA -> CSVWriteFailureException(message)
                else -> DataOperationException(message)
            }
        }
        GeneralOperationType.CONNECTION -> {
            when (operation) {
                CSVOperationName.OPEN_CSV_FILE -> CSVFileAccessException(message)
                else -> DataConnectionException(message)
            }
        }
        GeneralOperationType.PARSING -> {
            when (operation) {
                CSVOperationName.PARSE_CSV_ROW -> CSVParsingException(message)
                else -> DataParsingException(message)
            }
        }
        GeneralOperationType.CLOSING -> DataOperationException(message)
    }
}

private fun determineCSVOperationType(operation: CSVOperationName): GeneralOperationType = when (operation) {
    CSVOperationName.READ_CSV_DATA -> GeneralOperationType.RETRIEVAL
    CSVOperationName.WRITE_CSV_DATA -> GeneralOperationType.MODIFICATION
    CSVOperationName.PARSE_CSV_ROW -> GeneralOperationType.PARSING
    CSVOperationName.OPEN_CSV_FILE -> GeneralOperationType.CONNECTION
    CSVOperationName.CLOSE_CSV_FILE -> GeneralOperationType.CLOSING
}