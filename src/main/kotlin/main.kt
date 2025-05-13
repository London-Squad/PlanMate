import data.dataSources.mongoDBDataSource.DatabaseConnection
import di.dataModule
import di.logicModule
import di.mongoStorageModule
import di.uiModule
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.getKoin
import ui.welcomeView.WelcomeView

suspend fun main() {

    startKoin {
        modules(
//            csvStorageModule,
            mongoStorageModule,
            uiModule,
            dataModule,
            logicModule
        )
    }

    val ui: WelcomeView = getKoin().get()

    ui.start()

    // todo: make the app shutdown in a better way
    DatabaseConnection.close()

}
