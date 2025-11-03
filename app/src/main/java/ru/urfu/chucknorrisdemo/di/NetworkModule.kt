package ru.urfu.chucknorrisdemo.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.urfu.chucknorrisdemo.data.api.ChuckApi

private const val BASE_URL = "https://api.chucknorris.io/"

val networkModule = module {
    single {
        val interceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ChuckApi::class.java)
    }

    single<DataStore<Preferences>> {
        PreferenceDataStoreFactory.create(
            produceFile = { androidContext().preferencesDataStoreFile("chuck_prefs") }
        )
    }
}