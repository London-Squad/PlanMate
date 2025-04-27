package logic

import logic.entity.Project

interface ProjectsRepository {
    fun getAllProjects(): List<Project>
}