package ua.edu.chnu.labo15.di

import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module
import ua.edu.chnu.labo15.data.DatabaseDriverFactory

actual val platformModule: Module = module {
    // Android needs the application Context to open the SQLite database.
    single { DatabaseDriverFactory(androidContext()) }
}
