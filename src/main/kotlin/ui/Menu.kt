package ui

import logic.entity.User

abstract class Menu(
    private val activeUser: User
): View {
    abstract val viewOptions: List<View>
    abstract val content: String

    override fun start() {
        printContent()
        printOptions()
    }

    abstract fun printContent()

    private fun printOptions() {
        var i = 1
        viewOptions.forEach { view ->
            println("$i. ${view.optionMessageForMenus}")
        }
    }
}