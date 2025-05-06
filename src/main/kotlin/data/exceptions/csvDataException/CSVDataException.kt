package data.exceptions.csvDataException

import data.exceptions.DataAccessException

open class CSVDataException(message: String) : DataAccessException(message)

class FileNotFound : CSVDataException("CSV file could not be found or accessed")

class InvalidFormat : CSVDataException("CSV file has invalid format or corrupt data")

class WriteFailure : CSVDataException("Failed to write to CSV file")

class ValidationFailure : CSVDataException("CSV data failed validation checks")
