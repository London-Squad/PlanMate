import di.appModule
import di.presentationModule
import di.useCasesModule
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.getKoin
import ui.projectsView.ProjectsView

fun main() {
    startKoin {
        modules(appModule, useCasesModule, presentationModule)
    }
    val projectsView: ProjectsView = getKoin().get()
    projectsView.start()
}