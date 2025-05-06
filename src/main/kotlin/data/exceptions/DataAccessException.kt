package data.exceptions

open class DataAccessException(message: String) : Exception(message)

class NoLoggedInUserException(message: String = "No logged-in user found in cache") : DataAccessException(message)

class DataRetrievalFailureException(message: String = "Failed to retrieve data") : DataAccessException(message)
