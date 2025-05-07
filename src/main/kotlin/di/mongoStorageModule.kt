package di

import com.mongodb.client.MongoDatabase
import data.dataSources.*
import data.mongoDBDataSource.*
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

    single<TasksDataSource> { MongoDBTasksDataSource(get(named("tasksCollection")), get()) }
    single<TaskStatesDataSource> { MongoDBTaskStatesDataSource(get(named("taskStatesCollection")), get()) }
    single<ProjectsDataSource> { MongoDBProjectsDataSource(get(named("projectsCollection")), get()) }
    single<LogsDataSource> { MongoDBLogsDataSource(get(named("logsCollection")), get()) }
    single<UsersDataSource> { MongoDBUsersDataSource(get(named("usersCollection")), get()) }
}