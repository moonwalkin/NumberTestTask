package com.moonwalkin.numbertesttask.di

import android.content.Context
import androidx.room.Room
import com.moonwalkin.numbertesttask.BuildConfig
import com.moonwalkin.numbertesttask.data.database.NumberDao
import com.moonwalkin.numbertesttask.data.database.NumberDatabase
import com.moonwalkin.numbertesttask.data.network.NumberService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    fun providesNumberDao(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context,
            NumberDatabase::class.java,
            "numbers_history"
        )
            .build()
            .numberDao()

    @Provides
    fun providesNumberService(): NumberService {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder().add(
                        KotlinJsonAdapterFactory()
                    ).build()))
            .build()
            .create(NumberService::class.java)
    }

    @Provides
    @DefaultDispatcher
    fun providesDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @Provides
    @IoDispatcher
    fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @MainDispatcher
    fun providesMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

}