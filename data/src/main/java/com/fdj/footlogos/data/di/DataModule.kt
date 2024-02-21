package com.fdj.footlogos.data.di

import com.fdj.footlogos.data.LeagueRepository
import com.fdj.footlogos.data.LeagueRepositoryImpl
import com.fdj.footlogos.data.TeamRepository
import com.fdj.footlogos.data.TeamRepositoryImpl
import com.fdj.footlogos.network.NetworkDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Singleton
    @Provides
    fun provideLeagueRepository(
        networkDataSource: NetworkDataSource
    ): LeagueRepository = LeagueRepositoryImpl(networkDataSource)

    @Singleton
    @Provides
    fun provideTeamRepository(
        networkDataSource: NetworkDataSource
    ): TeamRepository = TeamRepositoryImpl(networkDataSource)
}