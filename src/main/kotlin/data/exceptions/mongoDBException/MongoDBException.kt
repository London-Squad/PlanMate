package data.exceptions.mongoDBException

import data.exceptions.DataAccessException

open class MongoDBException(message: String) : DataAccessException(message)

class ConnectionDBFailure(message: String = "Failed to connect to MongoDB") : MongoDBException(message)
