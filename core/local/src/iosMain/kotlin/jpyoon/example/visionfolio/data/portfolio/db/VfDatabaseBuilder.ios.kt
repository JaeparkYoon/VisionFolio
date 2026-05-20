package jpyoon.example.visionfolio.data.portfolio.db

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.Dispatchers
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSNumber
import platform.Foundation.NSURL
import platform.Foundation.NSURLIsExcludedFromBackupKey
import platform.Foundation.NSUserDomainMask

@OptIn(ExperimentalForeignApi::class)
fun createVfDatabase(): VfDatabase {
    val dbPath = documentDirectory() + "/" + VF_DATABASE_NAME
    excludeDatabaseFilesFromICloudBackup(dbPath)
    return Room.databaseBuilder<VfDatabase>(name = dbPath)
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.Default)
        .fallbackToDestructiveMigration(dropAllTables = true)
        .build()
}

@OptIn(ExperimentalForeignApi::class)
private fun documentDirectory(): String {
    val url = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null as NSURL?,
        create = true,
        error = null,
    )
    return requireNotNull(url?.path) { "Could not resolve iOS document directory" }
}

@OptIn(ExperimentalForeignApi::class)
private fun excludeDatabaseFilesFromICloudBackup(dbPath: String) {
    excludeFromICloudBackup(dbPath)
    excludeFromICloudBackup("$dbPath-wal")
    excludeFromICloudBackup("$dbPath-shm")
    excludeFromICloudBackup("$dbPath-journal")
}

@OptIn(ExperimentalForeignApi::class)
private fun excludeFromICloudBackup(path: String) {
    val fileManager = NSFileManager.defaultManager
    if (!fileManager.fileExistsAtPath(path)) {
        fileManager.createFileAtPath(path, contents = null, attributes = null)
    }
    val url = NSURL.fileURLWithPath(path)
    url.setResourceValue(
        value = NSNumber(bool = true),
        forKey = NSURLIsExcludedFromBackupKey,
        error = null,
    )
}
