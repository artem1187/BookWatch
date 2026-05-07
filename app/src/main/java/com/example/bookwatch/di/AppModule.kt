package com.example.bookwatch.di

import android.content.Context
import com.example.bookwatch.data.local.AppDatabase
import com.example.bookwatch.data.repository.BookRepositoryImpl
import com.example.bookwatch.domain.repository.BookRepository
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
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideBookRepository(
        database: AppDatabase
    ): BookRepository {
        return BookRepositoryImpl(database)
    }
}