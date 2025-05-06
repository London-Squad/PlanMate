package di

import com.mongodb.client.MongoDatabase
import data.csvDataSource.fileIO.CsvFileHandler
import data.dataSources.*
import data.mongoDBDataSource.*
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.io.File

val mongoStorageModule = module {
    single { DatabaseConnection }
    single<MongoDatabase> { DatabaseConnection.database }

    single(named("projectsCollection")) {
        DatabaseConnection.getProjectCollection()
    }
    single(named("logsCollection")) {
        DatabaseConnection.getLogsCollection()
    }
//    factory { DatabaseConnection.getTasksCollection() }
//    factory { DatabaseConnection.getTaskStatesCollection() }
//    factory { DatabaseConnection.getLogsCollection() }
//    factory { DatabaseConnection.getUsersCollection() }

//    single<TasksDataSource> { MongoDBTasksDataSource(get()) }
//    single<TasksStatesDataSource> { MongoDBTaskStatesDataSource(get()) }
    single<ProjectsDataSource> { MongoDBProjectsDataSource(get(named("projectsCollection"))) }
    single<LogsDataSource> { MongoDBLogsDataSource(get(named("logsCollection"))) }
//    single<UsersDataSource> { MongoDBUsersDataSource(get()) }
}