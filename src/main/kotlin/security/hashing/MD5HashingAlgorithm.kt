package security.hashing

import java.security.MessageDigest

class MD5HashingAlgorithm : HashingAlgorithm {
    @OptIn(ExperimentalStdlibApi::class)
    override fun hashData(data: String): String {
        val md5 = MessageDigest.getInstance("MD5")
        val hashedData = md5.digest(data.toByteArray()).toHexString()
        return hashedData
    }
}