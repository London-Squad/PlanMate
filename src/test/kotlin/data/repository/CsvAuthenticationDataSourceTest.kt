package data.repository

import com.google.common.truth.Truth.assertThat
import data.CsvAuthenticationDataSource
import data.fileIO.UserFileHelper
import data.fileIO.createFileIfNotExist
import data.security.hashing.HashingAlgorithm
import io.mockk.every
import io.mockk.mockk
import logic.exceptions.UserNotFoundException
import logic.repositories.AuthenticationRepository
import logic.repositories.CacheDataRepository
import logic.repository.DummyAuthData
import logic.validation.CredentialValidator
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.File
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

class CsvAuthenticationDataSourceTest {
    private lateinit var authenticationRepository: AuthenticationRepository
    private lateinit var file: File
    private lateinit var hashingAlgorithm: HashingAlgorithm
    private lateinit var credentialValidator: CredentialValidator
    private lateinit var cacheRepository: CacheDataRepository

    @BeforeTest
    fun preSetup() {
        file = File("test.csv")
        file.createFileIfNotExist( "id,userName,password\n")
    }

    @BeforeEach
    fun setup() {
        hashingAlgorithm = mockk(relaxed = true)
        credentialValidator = mockk(relaxed = true)
        cacheRepository = mockk(relaxed = true)
        authenticationRepository = CsvAuthenticationDataSource(file, hashingAlgorithm, credentialValidator, cacheRepository)
        every { hashingAlgorithm.hashData(any()) } answers {
            arg(0)
        }
    }

    @Test
    fun `when login with valid credentials of mate should return user`() {
        val user = DummyAuthData.users[1]
        UserFileHelper.writeUser(file, user.id, user.userName, "Poula12")

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

    @AfterTest
    fun teardown() {
        file.delete()
    }
}