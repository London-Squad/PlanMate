package ui.projectsView

import ui.utils.messageExitMenu
import ui.utils.readValidOption

class ProjectsView(
    private val handler: ProjectsViewHandler
) {
    fun start() {
        println("Welcome to Project Management System!\n")

        handler.displayProjects()

        do {
            showMenu()
            val choice = readValidOption("Enter your choice (0-5): ", MAX_OPTIONS)

            when (choice) {
                "1" -> handler.createProject()
                "2" -> handler.editTitleProject()
                "3" -> handler.editDescriptionProject()
                "4" -> handler.deleteProject()
                "5" -> handler.displayProjects()
                "0" -> {
                   messageExitMenu("Thank you for using the Project Management System!")
                    break
                }
                else -> println("Invalid choice. Please select a number between 0 and 5.\n")
            }
        } while (true)
    }

    private fun showMenu() {
        println("Please select an option:")
        println("1. Create a new project")
        println("2. Edit project title")
        println("3. Edit project description")
        println("4. Delete a project")
        println("5. Display all projects")
        println("0. Exit")
        println()
    }

    private companion object {
        const val MAX_OPTIONS = 5
    }
}