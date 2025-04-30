package logic.useCases

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import logic.entities.User
import logic.repositories.CacheDataRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

class GetActiveUserUseCaseTest{

     private lateinit var getActiveUserUseCase: GetActiveUserUseCase
     private lateinit var cacheDataRepository: CacheDataRepository

     @BeforeEach
     fun setup() {
         cacheDataRepository = mockk(relaxed = true)

         getActiveUserUseCase = GetActiveUserUseCase(cacheDataRepository)
     }

    @Test
    fun `getLoggedInUser should return null when no user is logged in`() {
        every { cacheDataRepository.getLoggedInUser() } returns null

        val result = getActiveUserUseCase.getLoggedInUser()

        assertThat(result).isNull()
    }

    @ParameterizedTest
    @MethodSource("getUsersList")
    fun `getLoggedInUser should return active user when user is logged in`(user:User) {
        every { cacheDataRepository.getLoggedInUser() } returns user

        val result = getActiveUserUseCase.getLoggedInUser()

        assertThat(result).isEqualTo(user)
    }

    private companion object {
        val fakeAdminUser = User(userName = "fake admin user", type = User.Type.ADMIN)
        val fakeMateUser = User(userName = "fake mate user", type = User.Type.MATE)
        @JvmStatic
        fun getUsersList(): List<User> = listOf(fakeAdminUser, fakeMateUser)
    }

 }