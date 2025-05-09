package data.exceptions

open class DataAccessException(message: String) : Exception(message)

class NoLoggedInUserException(message: String = "No logged-in user found in cache") : DataAccessException(message)

class DataRetrievalFailureException(message: String = "Failed to retrieve data") : DataAccessException(message)

class DataConnectionException(message: String = "Failed to connect to data source") : DataAccessException(message)

class DataOperationException(message: String = "Data operation failed") : DataAccessException(message)

class DataParsingException(message: String = "Failed to parse data") : DataAccessException(message)