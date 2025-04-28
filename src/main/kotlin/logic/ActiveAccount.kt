package logic

import logic.entity.User

object ActiveAccount {
    private var activeUser: User? = null

    fun getUser(): User = activeUser

    fun setUser(user: User) { activeUser = user }

}