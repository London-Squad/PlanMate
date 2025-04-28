package data.security.hashing

import data.security.hashing.exceptions.BlankDataException
import java.security.MessageDigest

class MD5HashingAlgorithm : HashingAlgorithm {
    @OptIn(ExperimentalStdlibApi::class)
    override fun hashData(data: String): String {
        if (data.isBlank()) throw BlankDataException()
        val md5 = MessageDigest.getInstance("MD5")
        val dataBytes = data.trim().toByteArray()
        val hashedData = md5.digest(dataBytes).toHexString()
        return hashedData
    }
}