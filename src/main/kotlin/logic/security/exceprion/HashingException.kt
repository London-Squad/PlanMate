package logic.security.exceprion

sealed class HashingException(message: String): Exception(message) {
    class InvalidDataException: HashingException("hashing function can't operate on empty data")
}