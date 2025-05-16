package data.dataSources.mongoDBDataSource

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import data.dto.*
import org.bson.UuidRepresentation


object DatabaseConnection {
    private const val DB_NAME = "LondonSquad-planMate-new"
    private const val PROJECTS_COLLECTION_NAME = "projects"
    private const val TASKS_COLLECTION_NAME = "tasks"
    private const val TASK_STATES_COLLECTION_NAME = "task_states"
    private const val LOGS_COLLECTION_NAME = "logs"
    private const val USERS_COLLECTION_NAME = "users"

    private val mongoUri: String = System.getenv("MONGO_URI")?: throw IllegalStateException("MONGO_URI environment variable is missing")

    var clientSettings: MongoClientSettings =
        MongoClientSettings.builder()
            .applyConnectionString(ConnectionString(mongoUri))
            .uuidRepresentation(UuidRepresentation.STANDARD).build()

    private val client = MongoClient.create(clientSettings)

    val database: MongoDatabase = client.getDatabase(DB_NAME)

    private val projectsCollection: MongoCollection<ProjectMongoDto> = database.getCollection(PROJECTS_COLLECTION_NAME)
    private val tasksCollection: MongoCollection<TaskMongoDto> = database.getCollection(TASKS_COLLECTION_NAME)
    private val taskStatesCollection: MongoCollection<TaskStateMongoDto> = database.getCollection(TASK_STATES_COLLECTION_NAME)
    private val logsCollection: MongoCollection<LogMongoDto> = database.getCollection(LOGS_COLLECTION_NAME)
    private val usersCollection: MongoCollection<UserMongoDto> = database.getCollection(USERS_COLLECTION_NAME)

    fun getProjectCollection(): MongoCollection<ProjectMongoDto> = projectsCollection
    fun getTasksCollection(): MongoCollection<TaskMongoDto> = tasksCollection
    fun getTaskStatesCollection(): MongoCollection<TaskStateMongoDto> = taskStatesCollection
    fun getLogsCollection(): MongoCollection<LogMongoDto> = logsCollection
    fun getUsersCollection(): MongoCollection<UserMongoDto> = usersCollection

    fun close() {
            client.close()
    }
}