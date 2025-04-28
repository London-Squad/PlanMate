package ui

import logic.entity.User

interface View {

    val optionMessageForMenus : String
    val UsersWithAccess: List<User.Type>
    fun start()
}