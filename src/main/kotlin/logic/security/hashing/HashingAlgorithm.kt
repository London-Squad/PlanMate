package logic.security.hashing

interface HashingAlgorithm {
    fun hashData(data: String): String
}