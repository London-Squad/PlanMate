package di

import com.mongodb.kotlin.client.coroutine.MongoDatabase
import data.dataSources.mongoDBDataSource.*
import data.dataSources.mongoDBDataSource.mongoDBParser.MongoDBParser
import data.repositories.dataSources.*
import logic.repositories.LogsRepository
import logic.repositories.ProjectsRepository
import logic.repositories.TaskRepository
import logic.repositories.TaskStatesRepository
import data.dataSources.mongoDBDataSource.mongoDBParser.MongoDBQueryHandler
import org.koin.core.qualifier.named
import org.koin.dsl.module

val mongoStorageModule = module {
    single { DatabaseConnection }
    single<MongoDatabase> { DatabaseConnection.database }

    single(named("projectsCollection")) {
        DatabaseConnection.getProjectCollection()
    }
    single(named("logsCollection")) {
        DatabaseConnection.getLogsCollection()
    }
    single(named("tasksCollection")) {
        DatabaseConnection.getTasksCollection()
    }
    single(named("taskStatesCollection")) {
        DatabaseConnection.getTaskStatesCollection()
    }
    single(named("usersCollection")) {
        DatabaseConnection.getUsersCollection()
    }

    single { MongoDBParser() }
    single(named("tasksQueryHandler")) {
        MongoDBQueryHandler(get(named("tasksCollection")))
    }
    single(named("projectsQueryHandler")) {
        MongoDBQueryHandler(get(named("projectsCollection")))
    }
    single(named("taskStatesQueryHandler")) {
        MongoDBQueryHandler(get(named("taskStatesCollection")))
    }
    single(named("usersQueryHandler")) {
        MongoDBQueryHandler(get(named("usersCollection")))
    }

    single(named("logsQueryHandler")) {
        MongoDBQueryHandler(get(named("logsCollection")))
    }
    single<TaskRepository> { MongoDBTasksDataSource(get(named("tasksQueryHandler")), get()) }
    single<TaskStatesRepository> { MongoDBTaskStatesDataSource(get(named("taskStatesQueryHandler")), get()) }
    single<ProjectsRepository> { MongoDBProjectsDataSource(get(named("projectsQueryHandler")), get()) }
    single<LogsRepository> { MongoDBLogsDataSource(get(named("logsQueryHandler")), get(), get(), get(), get()) }
    single<UsersDataSource> { MongoDBUsersDataSource(get(named("usersQueryHandler")), get()) }
}