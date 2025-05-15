package di

import com.mongodb.kotlin.client.coroutine.MongoDatabase
import data.dataSources.mongoDBDataSource.*
import data.dataSources.mongoDBDataSource.mongoDBHandler.MongoDBParser
import data.dataSources.mongoDBDataSource.mongoDBHandler.MongoDBQueryHandler
import data.repositories.dataSources.UsersDataSource
import logic.repositories.LogsRepository
import logic.repositories.ProjectsRepository
import logic.repositories.TaskRepository
import logic.repositories.TaskStatesRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module

val mongoStorageModule = module {
    single { DatabaseConnection }
    single<MongoDatabase> { DatabaseConnection.database }

    single(named("tasksQueryHandler")) {
        MongoDBQueryHandler(DatabaseConnection.getTasksCollection())
    }
    single(named("projectsQueryHandler")) {
        MongoDBQueryHandler(DatabaseConnection.getProjectCollection())
    }
    single(named("taskStatesQueryHandler")) {
        MongoDBQueryHandler(DatabaseConnection.getTaskStatesCollection())
    }
    single(named("usersQueryHandler")) {
        MongoDBQueryHandler(DatabaseConnection.getUsersCollection())
    }
    single(named("logsQueryHandler")) {
        MongoDBQueryHandler(DatabaseConnection.getLogsCollection())
    }
    single { MongoDBParser() }

    single<TaskRepository> { MongoDBTasksDataSource(get(named("tasksQueryHandler")), get()) }
    single<TaskStatesRepository> { MongoDBTaskStatesDataSource(get(named("taskStatesQueryHandler")), get()) }
    single<ProjectsRepository> { MongoDBProjectsDataSource(get(named("projectsQueryHandler")), get()) }
    single<LogsRepository> { MongoDBLogsDataSource(get(named("logsQueryHandler")), get()) }
    single<UsersDataSource> { MongoDBUsersDataSource(get(named("usersQueryHandler")), get()) }
}