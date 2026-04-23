package jpyoon.example.visionfolio.data.prefs

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

private const val PREFS_NAME = "visionfolio_prefs"

val Context.appPrefsDataStore: DataStore<Preferences> by preferencesDataStore(name = PREFS_NAME)
