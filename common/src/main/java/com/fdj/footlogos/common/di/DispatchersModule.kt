package com.fdj.footlogos.common.di

import com.fdj.footlogos.common.AppDispatchers
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
object DispatchersModule {
    @Provides
    fun providesAppDispatchers(): AppDispatchers = AppDispatchers(computation = Dispatchers.Default, io = Dispatchers.IO, main = Dispatchers.Main.immediate)
}
