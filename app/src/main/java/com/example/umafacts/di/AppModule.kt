package com.example.umafacts.di

import android.content.Context
import com.example.umafacts.database.UmaDatabase
import com.example.umafacts.repository.FavouritesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): UmaDatabase {
        return UmaDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideFavouriteDao(database: UmaDatabase) = database.favouriteDao()

    @Provides
    @Singleton
    fun provideFavouritesRepository(dao: com.example.umafacts.database.FavouriteDao): FavouritesRepository {
        return FavouritesRepository(dao)
    }
}