import di.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.getKoin
import ui.welcomeView.WelcomeView

fun main() {

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
    CoroutineScope(Dispatchers.Default).launch {
        ui.start()
    }
}
