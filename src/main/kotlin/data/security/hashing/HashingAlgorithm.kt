package data.security.hashing

interface HashingAlgorithm {
    fun hashData(data: String): String
}