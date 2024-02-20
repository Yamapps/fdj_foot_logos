package com.fdj.footlogos.ui.di

import android.content.Context
import com.fdj.footlogos.FootLogosApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideContext(application: FootLogosApplication): Context {
        return application.applicationContext
    }
}