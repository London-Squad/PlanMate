import di.*
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.getKoin
import ui.welcomeView.WelcomeView
import java.io.File

fun main() {
    startKoin {
        modules(
            csvStorageModule,
            // mongodbStorageModule,
            uiModule,
            dataModule,
            logicModule
        )
    }

    val ui: WelcomeView = getKoin().get()

    ui.start()
}
