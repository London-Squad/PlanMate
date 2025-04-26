package data.repository

import logic.AuthenticationRepository
import model.User
import java.security.MessageDigest

class FakeAuthenticationRepository: AuthenticationRepository {
    override fun login(userName: String, password: String): User {
        TODO("Not yet implemented")
    }

    override fun createMateAccount(userName: String, password: String): User {
        TODO("Not yet implemented")
    }

    private fun hashPasswordWithMD5(password: String): String {
        TODO("Not yet implemented")
    }
}