package logic

import model.Project

interface ProjectsRepository {
    fun getAllProjects(): List<Project>
}