package data.exceptions

open class DataAccessException(message: String) : Exception(message)

class NoLoggedInUser : DataAccessException("No logged-in user found in cache")

class DataRetrievalFailure : DataAccessException("Failed to retrieve data")
