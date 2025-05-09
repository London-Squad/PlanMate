package data.exceptions

import com.mongodb.MongoException
import data.exceptions.mapTypeException.*
import data.exceptions.sharedOperationTypes.CSVOperationName
import data.exceptions.sharedOperationTypes.MongoOperationName
import java.io.IOException

fun <T> handleException(operation: Any, block: () -> T): T {
    return try {
        block()
    } catch (e: MongoException) {
        when (operation) {
            is MongoOperationName -> throw mapMongoException(operation, e)
            else -> throw DataAccessException("MongoDB error encountered $operation: ${e.message}")
        }
    } catch (e: IOException) {
        when (operation) {
            is CSVOperationName -> throw mapCsvException(operation, e)
            else -> throw DataAccessException("IO error encountered CSV operation $operation: ${e.message}")
        }
    } catch (e: DataParsingException) {
        throw e
    } catch (e: Exception) {
        throw DataAccessException("Unexpected error during ${if (operation is MongoOperationName || operation is CSVOperationName) operation.toString().lowercase() else operation}: ${e.message}")
    }
}

