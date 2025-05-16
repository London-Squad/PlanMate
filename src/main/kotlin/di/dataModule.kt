package di

import data.dataSources.csvDataSource.CsvLoggedInUserCacheDataSource
import data.dataSources.csvDataSource.fileIO.CsvFileHandler
import data.dataSources.csvDataSource.fileIO.CsvParser
import data.repositories.*
import data.repositories.dataSources.LoggedInUserCacheDataSource
import data.security.hashing.HashingAlgorithm
import data.security.hashing.MD5HashingAlgorithm
import logic.repositories.*
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.io.File

val dataModule = module {
    single(named("LoggedInUserFileHandler")) {
        val directory = File("csvFiles")
        CsvFileHandler(File(directory, "LoggedInUser.csv"))
    }
    single { CsvParser() }
    single<LoggedInUserCacheDataSource> {
        CsvLoggedInUserCacheDataSource(get(named("LoggedInUserFileHandler")), get())
    }

    single<HashingAlgorithm> { MD5HashingAlgorithm() }
}