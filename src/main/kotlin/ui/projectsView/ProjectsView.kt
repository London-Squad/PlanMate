package ui.projectsView

import logic.entities.Project
import logic.usecases.ManageProjectUseCase

class ProjectsView(
    private val manageProjectUseCases: ManageProjectUseCase
) {

    fun start() {
        println("Welcome to Project Management System!\n")
        val projects = manageProjectUseCases.getAllProjects()
        printProjects(projects)
    }

    private fun printProjects(projects: List<Project>) {
        projects.ifEmpty {
            println("No projects found.")
            return
        }
        projects.forEach { project ->
            printProjectDetails(project)
        }
    }

    private fun printProjectDetails(project: Project) {
        println(
            """
             Project Details:
            - ID          : ${project.id}
            - Title       : ${project.title}
            - Description : ${project.description}
            """
        )
    }
}
