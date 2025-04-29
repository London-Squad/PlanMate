package data.catchData

import logic.repositories.CatchDataRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import com.google.common.truth.Truth.assertThat
import logic.entities.Project
import logic.entities.State
import logic.entities.Task
import logic.entities.User
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

class CatchDataMemoryRepositoryTest {
    private lateinit var catchDataRepository: CatchDataRepository

    @BeforeEach
    fun setup() {
        catchDataRepository = CatchDataMemoryRepository()
    }

//    @Test
//    fun `getLoggedInUser should return null when user is didn't log in`() {
//        val result = catchDataRepository.getLoggedInUser()
//
//        assertThat(result).isEqualTo(null)
//    }
//
//    @Test
//    fun `getLoggedInUser should return the stored selected user when user have been selected`() {
//        catchDataRepository.setLoggedInUser(fakeUser)
//
//        val result = catchDataRepository.getLoggedInUser()
//
//        assertThat(result).isEqualTo(fakeUser)
//    }
//
//    @Test
//    fun `clearLoggedInUserFromCatch should set the stored user to null` {
//        catchDataRepository.clearLoggedInUserFromCatch()
//
//        val result = catchDataRepository.getLoggedInUser()
//
//        assertThat(result).isEqualTo(null)
//    }
//
//    private companion object {
//        val fakeUser = User(userName = "fake admin user", type = User.Type.ADMIN)
//        val fakeTask = Task(title = "fake task", description = "no description")
//        val fakeState = State(title = "TO-DO", description = "")
//        val fakeProject = Project(
//            title = "fake project",
//            description = "",
//            tasks = listOf(fakeTask),
//            states = listOf(fakeState)
//        )
//        @JvmStatic
//        fun fakeUsers() = listOf(null, fakeUser)
//    }

}