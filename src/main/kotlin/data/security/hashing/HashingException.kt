package data.security.hashing

sealed class HashingException(message: String): Exception(message) {
    class BlankDataException: HashingException("hashing function can't operate on blank data")
}