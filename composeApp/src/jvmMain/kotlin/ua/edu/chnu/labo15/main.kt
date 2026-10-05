package ua.edu.chnu.labo15

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import ua.edu.chnu.labo15.di.initKoin
import ua.edu.chnu.labo15.ui.root.AppScaffold

fun main() {
    // Desktop/JVM entry point: start Koin before the UI is created.
    initKoin()

    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "labo15 Cross Platform",
        ) {
            AppScaffold()
        }
    }
}
