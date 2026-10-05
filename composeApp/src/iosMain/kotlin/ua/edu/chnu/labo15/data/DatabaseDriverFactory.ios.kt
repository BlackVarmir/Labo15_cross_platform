package ua.edu.chnu.labo15.data

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import ua.edu.chnu.labo15.db.AppDatabase

/**
 * iOS driver: backed by [NativeSqliteDriver], storing `reminders.db` in the
 * app sandbox. (iOS is not compiled on Windows, but kept for completeness.)
 */
actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver =
        NativeSqliteDriver(
            schema = AppDatabase.Schema,
            name = "reminders.db",
        )
}
