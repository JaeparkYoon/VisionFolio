package jpyoon.example.visionfolio.data.portfolio.db

import android.content.Context
import androidx.room.Room

fun createVfDatabase(context: Context): VfDatabase {
    val dbFile = context.applicationContext.getDatabasePath(VF_DATABASE_NAME)
    return createVfDatabaseBuilder(
        Room.databaseBuilder<VfDatabase>(
            context = context.applicationContext,
            name = dbFile.absolutePath,
        )
    )
}
