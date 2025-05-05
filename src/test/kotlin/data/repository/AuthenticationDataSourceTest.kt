package data.repository

import com.google.common.truth.Truth.assertThat
import data.AuthenticationDataSource
import data.fileIO.UserFileHelper
import data.security.hashing.HashingAlgorithm
import io.mockk.every
import io.mockk.mockk
import logic.entities.User
import logic.exceptions.UserAlreadyExistException
import logic.exceptions.UserNotFoundException
import logic.repositories.AuthenticationRepository
import logic.repository.DummyAuthData
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.File
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

class AuthenticationDataSourceTest {
    private lateinit var authenticationRepository: AuthenticationRepository
    private lateinit var UsersFile: File
    private lateinit var activeUserFile: File
    private lateinit var hashingAlgorithm: HashingAlgorithm

    @BeforeTest
    fun preSetup() {
        UsersFile = File("usersTest.csv")
        activeUserFile = File("activeUserTest.csv")
    }

    @AfterTest
    fun tearDown() {
        UsersFile.delete()
        activeUserFile.delete()
    }

    @BeforeEach
    fun setup() {
        hashingAlgorithm = mockk(relaxed = true)
        authenticationRepository = AuthenticationDataSource(UsersFile, activeUserFile, hashingAlgorithm)
        every { hashingAlgorithm.hashData(any()) } answers {
            arg(0)
        }
    }

    @Test
    fun `when call getMates should return list of users`() {
        val expectedUsers = DummyAuthData.users
        expectedUsers.forEach { UserFileHelper.writeUser(UsersFile, it.id, it.userName, "") }

        val actualUsers = authenticationRepository.getMates()

        assertThat(actualUsers).isEqualTo(expectedUsers)
    }

    @Test
    fun `when call getMates should return empty list`() {
        val expectedResult = listOf<User>()

        val actualUsers = authenticationRepository.getMates()

        assertThat(actualUsers).isEqualTo(expectedResult)
    }

    @Test
    fun `when call deleteUser with valid Id should delete it`() {
        val user = DummyAuthData.users[1]
        UserFileHelper.writeUser(UsersFile, user.id, user.userName, "")

        authenticationRepository.deleteUser(user.id)
        val lines = UsersFile.readLines()

        assertThat(lines).hasSize(1)
    }

    @Test
    fun `when login with valid credentials of mate should return user`() {
        val user = DummyAuthData.users[1]
        UserFileHelper.writeUser(UsersFile, user.id, user.userName, "Poula12")

        val loggedInUser = authenticationRepository.login(user.userName, "Poula12")

        assertThat(loggedInUser.userName).isEqualTo(user.userName)
        assertThat(loggedInUser.type).isEqualTo(loggedInUser.type)
    }

    @Test
    fun `when login with invalid credentials should throw exception`() {
        val username = "test"
        val password = "Test12"

        assertThrows<UserNotFoundException> {
            authenticationRepository.login(username, password)
        }
    }

    @Test
    fun `when login with valid credentials of Admin should return user`() {
        val loggedInUser = authenticationRepository.login("admin", "Admin12")

        assertThat(loggedInUser).isNotNull()
    }

    @Test
    fun `when we call logout should return true`() {
        val isLogout = authenticationRepository.logout()

        assertThat(isLogout).isTrue()
    }

    @Test
    fun `when we call register with valid credentials should return true`() {
        val user = DummyAuthData.users[1]

        val isRegistered = authenticationRepository.register(user.userName, "Password12")

        assertThat(isRegistered).isTrue()
    }

    @Test
    fun `when we call register with duplicate credentials should throw Exception`() {
        val user = DummyAuthData.users[1]
        authenticationRepository.register(user.userName, "Password12")

        assertThrows<UserAlreadyExistException> {
            authenticationRepository.register(user.userName, "Password12")
        }
    }

    @Test
    fun `when we call changePassword with valid user should return true`() {
        val user = DummyAuthData.users[2]
        UserFileHelper.writeUser(UsersFile, user.id, user.userName, "passworD12")

        val isChanged = authenticationRepository.changePassword(user.userName, "passworD12", "Password12")

        assertThat(isChanged).isTrue()
    }

    @Test
    fun `when we call changePassword with fake user should return exception`() {
        val user = DummyAuthData.users[2]

        assertThrows<UserNotFoundException> {
            authenticationRepository.changePassword(user.userName, "passworD12", "Password12")
        }
    }
}