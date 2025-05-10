package di

import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import data.dataSources.mongoDBDataSource.*
import data.dataSources.mongoDBDataSource.mongoDBParse.MongoDBParse
import data.repositories.dataSourceInterfaces.*
import org.bson.Document
import org.koin.core.qualifier.named
import org.koin.dsl.module

val mongoStorageModule = module {
    single { DatabaseConnection }
    single<MongoDatabase> { DatabaseConnection.database }

    single<MongoCollection<Document>>(named("projectsCollection")) {
        DatabaseConnection.getProjectCollection()
    }
    single<MongoCollection<Document>>(named("logsCollection")) {
        DatabaseConnection.getLogsCollection()
    }
    single<MongoCollection<Document>>(named("tasksCollection")) {
        DatabaseConnection.getTasksCollection()
    }
    single<MongoCollection<Document>>(named("taskStatesCollection")) {
        DatabaseConnection.getTaskStatesCollection()
    }
    single<MongoCollection<Document>>(named("usersCollection")) {
        DatabaseConnection.getUsersCollection()
    }

    single { MongoDBParse() }

    single<TasksDataSource> { MongoDBTasksDataSource(get(named("tasksCollection")), get()) }
    single<TaskStatesDataSource> { MongoDBTaskStatesDataSource(get(named("taskStatesCollection")), get()) }
    single<ProjectsDataSource> { MongoDBProjectsDataSource(get(named("projectsCollection")), get()) }
    single<LogsDataSource> { MongoDBLogsDataSource(get(named("logsCollection")), get()) }
    single<UsersDataSource> { MongoDBUsersDataSource(get(named("usersCollection")), get()) }
}