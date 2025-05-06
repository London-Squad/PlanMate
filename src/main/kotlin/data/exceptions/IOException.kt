package data.exceptions

open class IOException(message: String) : DataAccessException(message)

class IOAccessFailure(message: String = "File or resource could not be accessed") : IOException(message)
class IOReadFailure(message: String = "Failed to read from file or resource") : IOException(message)
class IOWriteFailure(message: String = "Failed to write to file or resource") : IOException(message)
