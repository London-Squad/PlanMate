package di

import com.mongodb.client.MongoDatabase
import data.dataSources.mongoDBDataSource.*
import data.dataSources.mongoDBDataSource.mongoDBParse.MongoDBParse
import data.repositories.dataSourceInterfaces.*
import logic.repositories.LogsRepository
import logic.repositories.ProjectsRepository
import logic.repositories.TaskRepository
import logic.repositories.TaskStatesRepository
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

    single { MongoDBParse() }

    single<TaskRepository> { MongoDBTasksDataSource(get(named("tasksCollection")), get()) }

    single<TaskStatesRepository> { MongoDBTaskStatesDataSource(get(named("taskStatesCollection")), get()) }
    single<ProjectsRepository> { MongoDBProjectsDataSource(get(named("projectsCollection")), get()) }
    single<LogsRepository> { MongoDBLogsDataSource(get(named("logsCollection")), get(),get(),get(),get()) }
    single<UsersDataSource> { MongoDBUsersDataSource(get(named("usersCollection")), get()) }
}