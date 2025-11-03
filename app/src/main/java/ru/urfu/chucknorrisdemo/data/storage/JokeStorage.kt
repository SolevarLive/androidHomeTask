package ru.urfu.chucknorrisdemo.data.storage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class JokeStorage(
    private val dataStore: DataStore<Preferences>
) {
    private val LAST_JOKE_KEY = stringPreferencesKey("last_joke")

    fun getJoke(): Flow<String> = dataStore.data.map { prefs ->
        prefs[LAST_JOKE_KEY] ?: ""
    }

    suspend fun saveJoke(joke: String) {
        dataStore.edit { prefs ->
            prefs[LAST_JOKE_KEY] = joke
        }
    }
}