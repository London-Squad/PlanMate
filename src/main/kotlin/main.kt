import di.uiModule
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.getKoin
import ui.welcomeView.WelcomeView

fun main() {
    startKoin {
        modules(uiModule)
    }
    val ui: WelcomeView = getKoin().get()
    ui.startWelcome()
}