package data.catchData

import logic.repositories.CatchDataRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import com.google.common.truth.Truth.assertThat

class CatchDataMemoryRepositoryTest {
    private lateinit var catchDataRepository: CatchDataRepository

    @BeforeEach
    fun setup() {
        catchDataRepository = CatchDataMemoryRepository()
    }
//
//    @Test
//    fun `getLoggedInUser should return null when user is not selected`() {
//        val result = catchDataRepository.getLoggedInUser()
//
//        assertThat(result).isEqualTo(null)
//    }
//
//    @Test
//    fun `getLoggedInUser should return null when user is not selected`() {
//        val result = catchDataRepository.getLoggedInUser()
//
//        assertThat(result).isEqualTo(null)
//    }

}