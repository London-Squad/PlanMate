package data.repository

import com.google.common.truth.Truth.assertThat
import data.CsvMateDataSource
import data.fileIO.UserFileHelper
import data.fileIO.createFileIfNotExist
import data.security.hashing.HashingAlgorithm
import io.mockk.mockk
import logic.entities.User
import logic.exceptions.UserAlreadyExistException
import logic.exceptions.UserNotFoundException
import logic.repositories.MateRepository
import logic.repository.DummyAuthData
import logic.validation.CredentialValidator
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import java.io.File
import kotlin.test.AfterTest

import kotlin.test.BeforeTest
import kotlin.test.Test

class CsvMateDataSourceTest {
    private lateinit var mateRepository: MateRepository
    private lateinit var file: File
    private lateinit var hashingAlgorithm: HashingAlgorithm
    private lateinit var credentialValidator: CredentialValidator

    @BeforeTest
    fun preSetup() {
        file = File("test.csv")
        file.createFileIfNotExist("id,userName,password\n")
    }

    @BeforeEach
    fun setUp() {
        hashingAlgorithm = mockk(relaxed = true)
        credentialValidator = mockk(relaxed = true)
        mateRepository = CsvMateDataSource(file, hashingAlgorithm, credentialValidator)
    }

    @Test
    fun `when call getMates should return list of users`() {
        val expectedUsers = DummyAuthData.users
        expectedUsers.forEach { UserFileHelper.writeUser(file, it.id, it.userName, "") }

        val actualUsers = mateRepository.getMates()

        assertThat(actualUsers).isEqualTo(expectedUsers)
    }

    @Test
    fun `when call getMates should return empty list`() {
        val expectedResult = listOf<User>()

        val actualUsers = mateRepository.getMates()

        assertThat(actualUsers).isEqualTo(expectedResult)
    }

    @Test
    fun `when call deleteUser with valid Id should delete it`() {
        val user = DummyAuthData.users[1]
        UserFileHelper.writeUser(file, user.id, user.userName, "")

        mateRepository.deleteUser(user.id)
        val lines = file.readLines()

        assertThat(lines).hasSize(1)
    }

    @Test
    fun `when we call register with valid credentials should return true`() {
        val user = DummyAuthData.users[1]

        val isRegistered = mateRepository.addMate(user.userName, "Password12")

        assertThat(isRegistered).isTrue()
    }

    @Test
    fun `when we call register with duplicate credentials should throw Exception`() {
        val user = DummyAuthData.users[1]
        mateRepository.addMate(user.userName, "Password12")

        assertThrows<UserAlreadyExistException> {
            mateRepository.addMate(user.userName, "Password12")
        }
    }

    @Test
    fun `when we call changePassword with valid user should return true`() {
        val user = DummyAuthData.users[2]
        UserFileHelper.writeUser(file, user.id, user.userName, "passworD12")

        val isChanged = mateRepository.changeMatePassword(user.userName, "passworD12", "Password12")

        assertThat(isChanged).isTrue()
    }

    @Test
    fun `when we call changePassword with fake user should return exception`() {
        val user = DummyAuthData.users[2]

        assertThrows<UserNotFoundException> {
            mateRepository.changeMatePassword(user.userName, "passworD12", "Password12")
        }
    }

    @AfterTest
    fun tearDown() {
        file.delete()
    }
}