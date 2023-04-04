package com.example.newsapplearningmvvm.di

import android.app.Application
import androidx.room.Room
import com.example.newsapplearningmvvm.db.MainDb
import com.example.newsapplearningmvvm.network.ApiService
import com.example.newsapplearningmvvm.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainModule {

    @Provides
    fun provideInterceptor() =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    @Provides
    fun provideClient() = OkHttpClient.Builder()
        .addInterceptor(provideInterceptor())
        .build()

    @Provides
    @Singleton
    fun provideApiService():ApiService =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(provideClient())
            .build()
            .create(ApiService::class.java)

    @Provides
    @Singleton
    fun provideMainDb(app:Application):MainDb =
        Room.databaseBuilder(app,MainDb::class.java,"news.db")
            .fallbackToDestructiveMigration()
            .build()
}