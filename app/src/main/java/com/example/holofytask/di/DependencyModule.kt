package com.example.holofytask.di

import com.example.holofytask.data.repositories.VideoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DependencyModule {

    @Singleton
    @Provides
    fun provideVideoRepository() = VideoRepository()

}