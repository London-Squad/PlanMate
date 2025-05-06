package data.mongoDBDataSource

import com.mongodb.client.MongoCollection
import data.dataSources.ProjectsDataSource
import data.dto.ProjectDto
import org.bson.Document
import java.util.*

class MongoDBProjectsDataSource(
    private val collection: MongoCollection<Document>
) : ProjectsDataSource {
    override fun getAllProjects(): List<ProjectDto> {
        return collection.find().map { doc ->
            ProjectDto(
                id = UUID.fromString(doc.getString(ID_FIELD)),
                title = doc.getString(TITLE_FIELD),
                description = doc.getString(DESCRIPTION_FIELD),
                isDeleted = doc.getBoolean(IS_DELETED_FIELD)
            )
        }.toList()
    }

    override fun addNewProject(project: ProjectDto) {
        val doc = Document(ID_FIELD, project.id.toString())
            .append(TITLE_FIELD, project.title)
            .append(DESCRIPTION_FIELD, project.description)
            .append(IS_DELETED_FIELD, project.isDeleted)
        collection.insertOne(doc)
    }

    override fun editProjectTitle(projectId: UUID, newTitle: String) {
        val updateDoc = Document("\$set", Document(TITLE_FIELD, newTitle))
        collection.updateOne(Document(ID_FIELD, projectId.toString()), updateDoc)
    }

    override fun editProjectDescription(projectId: UUID, newDescription: String) {
        val updateDoc = Document("\$set", Document(DESCRIPTION_FIELD, newDescription))
        collection.updateOne(Document(ID_FIELD, projectId.toString()), updateDoc)
    }

    override fun deleteProject(projectId: UUID) {
        collection.deleteOne(Document(ID_FIELD, projectId.toString()))
    }

    companion object {
        private const val ID_FIELD = "id"
        private const val TITLE_FIELD = "title"
        private const val DESCRIPTION_FIELD = "description"
        private const val IS_DELETED_FIELD = "isDeleted"
    }
}