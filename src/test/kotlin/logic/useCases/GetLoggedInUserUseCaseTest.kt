package logic.useCases

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import logic.entities.User
import logic.repositories.CacheDataRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

class GetLoggedInUserUseCaseTest {

    private lateinit var getLoggedInUserUseCase: GetLoggedInUserUseCase
     private lateinit var cacheDataRepository: CacheDataRepository

     @BeforeEach
     fun setup() {
         cacheDataRepository = mockk(relaxed = true)

         getLoggedInUserUseCase = GetLoggedInUserUseCase(cacheDataRepository)
     }

    @ParameterizedTest
    @MethodSource("getUsersList")
    fun `getLoggedInUser should return logged in user when user is logged in`(user: User) {
        every { cacheDataRepository.getLoggedInUser() } returns user

        val result = getLoggedInUserUseCase.getLoggedInUser()

        assertThat(result).isEqualTo(user)
    }

    private companion object {
        val fakeAdminUser = User(userName = "fake admin user", type = User.Type.ADMIN)
        val fakeMateUser = User(userName = "fake mate user", type = User.Type.MATE)
        @JvmStatic
        fun getUsersList(): List<User> = listOf(fakeAdminUser, fakeMateUser)
    }

 }