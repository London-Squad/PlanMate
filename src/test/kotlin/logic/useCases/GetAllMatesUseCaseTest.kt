package logic.useCases

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import logic.entities.User
import logic.repositories.UserRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class GetAllMatesUseCaseTest {

    private lateinit var userRepository: UserRepository
    private lateinit var getAllMatesUseCase: GetAllMatesUseCase

    @BeforeEach
    fun setUp() {
        userRepository = mockk(relaxed = true)
        getAllMatesUseCase = GetAllMatesUseCase(userRepository)
    }

    @Test
    fun `getAllMates should return list of mates when repository returns mates`() = runTest {
        val mates = listOf(fakeData.mate)
        coEvery { userRepository.getMates() } returns mates

        val result = getAllMatesUseCase.getAllMates()

        assertEquals(mates, result)
        coVerify { userRepository.getMates() }
    }

    @Test
    fun `getAllMates should return empty list when repository returns no mates`() = runTest {
        val mates = emptyList<User>()
        coEvery { userRepository.getMates() } returns mates

        val result = getAllMatesUseCase.getAllMates()

        assertEquals(mates, result)
        coVerify { userRepository.getMates() }
    }
}