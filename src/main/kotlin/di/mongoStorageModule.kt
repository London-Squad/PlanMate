package di

import com.mongodb.kotlin.client.coroutine.MongoDatabase
import data.dataSources.mongoDBDataSource.*
import data.dataSources.mongoDBDataSource.mongoDBParse.MongoDBParse
import data.dto.UserMongoDto
import data.repositories.AuthenticationRepositoryImpl
import data.repositories.UserRepositoryImpl
import data.repositories.dataSources.*
import data.repositories.dtoMappers.user.UseMongoDtoMapper
import data.repositories.dtoMappers.user.UserDtoMapper
import logic.repositories.*
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

    single<TaskRepository> { MongoDBTasksDataSource(get(named("tasksCollection"))) }

    single<TaskStatesRepository> { MongoDBTaskStatesDataSource(get(named("taskStatesCollection"))) }
    single<ProjectsRepository> { MongoDBProjectsDataSource(get(named("projectsCollection"))) }
    single<LogsRepository> { MongoDBLogsDataSource(get(named("logsCollection")), get(), get(), get()) }
    single<UsersDataSource<UserMongoDto>> { MongoDBUsersDataSource(get(named("usersCollection"))) }

    single<UserDtoMapper<UserMongoDto>> { UseMongoDtoMapper() }
    single<AuthenticationRepository> { AuthenticationRepositoryImpl<UserMongoDto>(get(), get(), get(), get()) }
    single<UserRepository> { UserRepositoryImpl<UserMongoDto>(get(), get(), get()) }
}