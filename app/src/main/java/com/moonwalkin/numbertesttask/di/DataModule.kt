package com.moonwalkin.numbertesttask.di

import android.content.Context
import androidx.room.Room
import com.moonwalkin.numbertesttask.BuildConfig
import com.moonwalkin.numbertesttask.data.database.NumberDatabase
import com.moonwalkin.numbertesttask.data.network.NumberService
import com.moonwalkin.numbertesttask.data.repository.NumberRepositoryImpl
import com.moonwalkin.numbertesttask.domain.NumberRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun providesNumberDao(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context,
            NumberDatabase::class.java,
            "numbers_history"
        )
            .build()
            .numberDao()

    @Provides
    @Singleton
    fun providesNumberService(client: OkHttpClient): NumberService {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder().add(
                        KotlinJsonAdapterFactory()
                    ).build()
                )
            )
            .client(client)
            .build()
            .create(NumberService::class.java)
    }

    @Provides
    @Singleton
    fun providesOkHttp() = OkHttpClient.Builder()
        .addInterceptor(
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        ).build()

    @Provides
    @Singleton
    fun providesRepository(impl: NumberRepositoryImpl): NumberRepository = impl

    @Provides
    @DefaultDispatcher
    fun providesDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @Provides
    @IoDispatcher
    fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @MainDispatcher
    fun providesMainDispatcher(): CoroutineDispatcher = Dispatchers.Main.immediate

}