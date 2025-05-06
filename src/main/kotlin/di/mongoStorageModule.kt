package di

import com.mongodb.client.MongoDatabase
import data.dataSources.*
import data.mongoDBDataSource.*
import org.koin.dsl.module

val mongoStorageModule = module {
    single { DatabaseConnection }
    single<MongoDatabase> { DatabaseConnection.database }
    factory { DatabaseConnection.getProjectCollection() }
//    factory { DatabaseConnection.getTasksCollection() }
//    factory { DatabaseConnection.getTaskStatesCollection() }
    factory { DatabaseConnection.getLogsCollection() }
//    factory { DatabaseConnection.getUsersCollection() }

//    single<TasksDataSource> { MongoDBTasksDataSource(get()) }
//    single<TasksStatesDataSource> { MongoDBTaskStatesDataSource(get()) }
    single<ProjectsDataSource> { MongoDBProjectsDataSource(get()) }
    single<LogsDataSource> { MongoDBLogsDataSource(get()) }
//    single<UsersDataSource> { MongoDBUsersDataSource(get()) }
}