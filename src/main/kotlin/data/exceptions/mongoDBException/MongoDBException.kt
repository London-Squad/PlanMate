package data.exceptions.mongoDBException

import data.exceptions.DataAccessException

open class MongoDBException(message: String) : DataAccessException(message)

class ConnectionDBFailure : MongoDBException("Failed to connect to MongoDB")
