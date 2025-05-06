package data.exceptions.csvDataException

import data.exceptions.DataAccessException

open class CSVDataException(message: String) : DataAccessException(message)

class FileNotFound(message: String = "CSV file could not be found or accessed") : CSVDataException(message)

class InvalidFormat(message: String = "CSV file has invalid format or corrupt data") : CSVDataException(message)

class WriteFailure(message: String = "Failed to write to CSV file") : CSVDataException(message)

class ValidationFailure(message: String = "CSV data failed validation checks") : CSVDataException(message)
