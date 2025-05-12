package data.dataSources.mongoDBDataSource

import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import org.bson.Document

object DatabaseConnection {
    private const val DB_NAME = "LondonSquad-PlanMate"
    private const val PROJECTS_COLLECTION_NAME = "projects"
    private const val TASKS_COLLECTION_NAME = "tasks"
    private const val TASK_STATES_COLLECTION_NAME = "task_states"
    private const val LOGS_COLLECTION_NAME = "logs"
    private const val USERS_COLLECTION_NAME = "users"

    private val mongoUri: String = System.getenv("MONGO_URI")?: throw IllegalStateException("MONGO_URI environment variable is missing")

    private val client = MongoClient.create(mongoUri)

    val database: MongoDatabase = client.getDatabase(DB_NAME)

    private val projectsCollection: MongoCollection<Document> = database.getCollection(PROJECTS_COLLECTION_NAME)
    private val tasksCollection: MongoCollection<Document> = database.getCollection(TASKS_COLLECTION_NAME)
    private val taskStatesCollection: MongoCollection<Document> = database.getCollection(TASK_STATES_COLLECTION_NAME)
    private val logsCollection: MongoCollection<Document> = database.getCollection(LOGS_COLLECTION_NAME)
    private val usersCollection: MongoCollection<Document> = database.getCollection(USERS_COLLECTION_NAME)

    fun getProjectCollection(): MongoCollection<Document> = projectsCollection
    fun getTasksCollection(): MongoCollection<Document> = tasksCollection
    fun getTaskStatesCollection(): MongoCollection<Document> = taskStatesCollection
    fun getLogsCollection(): MongoCollection<Document> = logsCollection
    fun getUsersCollection(): MongoCollection<Document> = usersCollection

    fun close() {
            client.close()
    }
}