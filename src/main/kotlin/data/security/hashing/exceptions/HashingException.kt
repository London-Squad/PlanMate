package data.security.hashing.exceptions

import logic.exceptions.StoringDataFailureException

open class HashingException(message: String): StoringDataFailureException(message)