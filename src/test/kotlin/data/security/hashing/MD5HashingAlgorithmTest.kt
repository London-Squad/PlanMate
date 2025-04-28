package data.security.hashing

import com.google.common.truth.Truth.assertThat
import data.security.hashing.exceptions.BlankDataException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class MD5HashingAlgorithmTest {
    private lateinit var md5HashingAlgorithm: HashingAlgorithm

    @BeforeEach
    fun setup() {
        md5HashingAlgorithm = MD5HashingAlgorithm()
    }

    @ParameterizedTest
    @CsvSource(
        "Alqassim#!3@45,c9bb8568d461812ab45072a7a6183a9c",
        "Poula\$1^0@2,3f4a27bb6db572a798e15c9bad58d43c",
        "paSSowrd(TesT),ad08543fe0405c02df55bca8a7dbefad",
    )
    fun `when calling hashData with data should return md5 hashed data`(data: String, expectedHashedData: String) {
        val actualHashedData = md5HashingAlgorithm.hashData(data)
        assertThat(actualHashedData).isEqualTo(expectedHashedData)
    }

    @Test
    fun `when calling hashData with empty string should throw exception`() {
        val data = ""

        assertThrows<BlankDataException> {
            md5HashingAlgorithm.hashData(data)
        }
    }

    @Test
    fun `when calling hashData with white spaces string should throw exception`() {
        val data = "      "

        assertThrows<BlankDataException> {
            md5HashingAlgorithm.hashData(data)
        }
    }
}