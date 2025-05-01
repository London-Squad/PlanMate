import di.*
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.getKoin
import ui.welcomeView.WelcomeView

fun main() {
    startKoin {
        modules(dataModule, logicModule, uiModule)
        modules(appModule, repoModules, uiModule, useCaseModule)

    }
    val ui: WelcomeView = getKoin().get()
    ui.startWelcome()
}