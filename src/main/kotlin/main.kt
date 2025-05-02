import di.*
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.getKoin
import ui.welcomeView.WelcomeView
import java.io.File

fun main() {
    startKoin {
        modules(uiModule, dataModule, logicModule)

    }
    initFilesDirectory()
    val ui: WelcomeView = getKoin().get()
    ui.start()
}

fun initFilesDirectory() {
    val directory = File("csvFiles")
    if (!directory.exists()) directory.mkdir()
}
