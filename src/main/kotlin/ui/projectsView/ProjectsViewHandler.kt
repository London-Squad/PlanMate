package ui.projectsView

import data.cacheData.CacheDataRepositoryImpl
import logic.entities.Project
import logic.usecases.ManageProjectUseCase
import ui.utils.formatProjectDetails
import ui.utils.readUserInput
import ui.utils.tableHeader
import java.util.*

class ProjectsViewHandler(
    private val manageProjectUseCases: ManageProjectUseCase,
) {
    fun displayProjects() {
        println("\n ================= Display All Projects ================= ")
        val projects = manageProjectUseCases.getAllProjects()
        projects.ifEmpty {
            println("         No projects found.           ")
            return
        }
        println(tableHeader())
        projects.forEach { project ->
            println(formatProjectDetails(project))
        }
    }

    fun createProject(): Boolean {
        println("\n  ================= Create a New Project  ================= ")
        val title = readUserInput("Enter project title: ")
            ?: return false.also { println("Invalid title. Operation cancelled.\n") }
        val description = readUserInput("Enter project description: ")
            ?: return false.also { println("Invalid description. Operation cancelled.\n") }
        val newProject = Project(
            title = title, description = description, tasks = emptyList(), states = emptyList()
        )
        manageProjectUseCases.addNewProject(newProject)
        println("Project '${newProject.title}' created successfully!\n")
        return true
    }

    fun editTitleProject(): Boolean {
        println("\n================= Edit Project Title ================= ")
        val project = getProjectForEdit() ?: return false

        println("Current Project Details:")
        println(tableHeader())
        println(formatProjectDetails(project))

        val newTitle =
            readUserInput("Enter new project title (or press Enter to keep '${project.title}'): ") ?: project.title
        return try {
            manageProjectUseCases.editProjectTitle(project.id, newTitle)
            println("Project title updated successfully!\n")
            true
        } catch (e: Exception) {
            println("Failed to update project title: ${e.message}\n")
            false
        }
    }

    fun editDescriptionProject(): Boolean {
        println("\n================= Edit Project Description ================= ")
        val project = getProjectForEdit() ?: return false

        println("Current Project Details:")
        println(tableHeader())
        println(formatProjectDetails(project))

        val newDescription =
            readUserInput("Enter new project description (or press Enter to keep '${project.description}'): ")
                ?: project.description
        return try {
            manageProjectUseCases.editProjectDescription(project.id, newDescription)
            println("Project description updated successfully!\n")
            true
        } catch (e: Exception) {
            println("Failed to update project description: ${e.message}\n")
            false
        }
    }


    fun deleteProject(): Boolean {
        println("\n================= Delete a Project ================= ")
        val projectIdUUID = readProjectId() ?: return false
        val project =
            findProject(projectIdUUID) ?: return false.also { println("Project with ID $projectIdUUID not found.\n") }

        return try {
            manageProjectUseCases.deleteProject(projectIdUUID)
            println("Project '${project.title}' deleted successfully!\n")
            true
        } catch (e: Exception) {
            println("Failed to delete project: ${e.message}\n")
            false
        }
    }


    private fun readProjectId(): UUID? {
        val projectId =
            readUserInput("Enter project ID: ") ?: return null.also { println("Invalid ID. Operation cancelled.\n") }
        return try {
            UUID.fromString(projectId)
        } catch (e: IllegalArgumentException) {
            println("Invalid project ID format. Operation cancelled.\n")
            null
        }
    }

    private fun getProjectForEdit(): Project? {
        val projectIdUUID = readProjectId() ?: return null
        return findProject(projectIdUUID) ?: return null.also { println("Project with ID $projectIdUUID not found.\n") }
    }

    private fun findProject(projectId: UUID): Project? {
        return manageProjectUseCases.getAllProjects().find { it.id == projectId }
    }
}