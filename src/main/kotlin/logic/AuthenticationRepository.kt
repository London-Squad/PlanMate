package logic

import model.User

interface AuthenticationRepository {

    fun login(userName: String, password: String): User

    fun createMateAccount(userName: String, password: String): User

}