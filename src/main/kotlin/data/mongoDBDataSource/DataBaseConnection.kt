package data.mongoDBDataSource

import com.mongodb.reactivestreams.client.MongoClients
import com.mongodb.reactivestreams.client.MongoCollection
import com.mongodb.reactivestreams.client.MongoDatabase
import org.bson.Document

object DataBaseConnection {
    private const val dbName = "LondonSquad-PlanMate"
    private const val projectsCollectionName = "projects"
    private const val tasksCollectionName = "tasks"
    private const val taskStatesCollectionName = "task_states"
    private const val logsCollectionName = "logs"

    private val client =
        MongoClients.create("mongodb+srv://LondonSquad:MKMa6ETmaNz8Asnl@cluster0.qibabiq.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0")
    private val database: MongoDatabase = client.getDatabase(dbName)
    private val projectsCollection: MongoCollection<Document> = database.getCollection(projectsCollectionName)
    private val tasksCollection: MongoCollection<Document> = database.getCollection(tasksCollectionName)
    private val taskStatesCollection: MongoCollection<Document> = database.getCollection(taskStatesCollectionName)
    private val logsCollection: MongoCollection<Document> = database.getCollection(logsCollectionName)

    fun getProjectCollection(): MongoCollection<Document> = projectsCollection
    fun getTasksCollection(): MongoCollection<Document> = tasksCollection
    fun getTaskStatesCollection(): MongoCollection<Document> = taskStatesCollection
    fun getLogsCollection(): MongoCollection<Document> = logsCollection
}