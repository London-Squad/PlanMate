package data.mongoDBDataSource

import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import data.dataSources.ProjectsDataSource
import data.dto.ProjectDto
import org.bson.Document
import java.util.UUID

class MongoDBProjectsDataSource(
    private val projectsCollection: MongoCollection<Document> = DatabaseConnection.getUsersCollection()
) : ProjectsDataSource {

    companion object {
        private const val ID_FIELD = "id"
        private const val TITLE_FIELD = "title"
        private const val DESCRIPTION_FIELD = "description"
        private const val IS_DELETED_FIELD = "isDeleted"
    }

    override fun getAllProjects(): List<ProjectDto> {
        return projectsCollection.find().map { doc ->
            ProjectDto(
                id = UUID.fromString(doc.getString(ID_FIELD)),
                title = doc.getString(TITLE_FIELD),
                description = doc.getString(DESCRIPTION_FIELD),
                isDeleted = doc.getBoolean(IS_DELETED_FIELD) ?: false
            )
        }.toList()
    }

    override fun addNewProject(project: ProjectDto) {
        val doc = Document(ID_FIELD, project.id.toString())
            .append(TITLE_FIELD, project.title)
            .append(DESCRIPTION_FIELD, project.description)
            .append(IS_DELETED_FIELD, project.isDeleted)
        projectsCollection.insertOne(doc)
    }

    override fun editProjectTitle(projectId: UUID, newTitle: String) {
        projectsCollection.updateOne(Filters.eq(ID_FIELD, projectId.toString()), Updates.set(TITLE_FIELD, newTitle))
    }

    override fun editProjectDescription(projectId: UUID, newDescription: String) {
        projectsCollection.updateOne(
            Filters.eq(ID_FIELD, projectId.toString()),
            Updates.set(DESCRIPTION_FIELD, newDescription)
        )
    }

    override fun deleteProject(projectId: UUID) {
        projectsCollection.updateOne(
            Filters.eq(ID_FIELD, projectId.toString()),
            Updates.set(IS_DELETED_FIELD, true)
        )
    }
}