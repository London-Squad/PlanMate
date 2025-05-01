import di.dataModule
import di.logicModule
import di.uiModule
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.getKoin
import ui.welcomeView.WelcomeView
import java.io.File

fun main() {
    startKoin {
        modules(dataModule, logicModule, uiModule)
    }
    initFilesDirectory()
    val ui: WelcomeView = getKoin().get()
    ui.startWelcome()
}

fun initFilesDirectory() {
    val directory = File("csvFiles")
    if (!directory.exists()) directory.mkdir()
}
