package com.fdj.footlogos.di

import com.fdj.footlogos.data.FakeLeagueRepositoryImpl
import com.fdj.footlogos.data.FakeTeamRepositoryImpl
import com.fdj.footlogos.data.LeagueRepository
import com.fdj.footlogos.data.TeamRepository
import com.fdj.footlogos.data.di.DataModule
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DataModule::class]
)
abstract class DataTestModule {

    @Singleton
    @Binds
    abstract fun bindsFakeLeagueRepository(fakeLeagueRepositoryImpl: FakeLeagueRepositoryImpl): LeagueRepository

    @Singleton
    @Binds
    abstract fun bindsFakeTeamRepository(fakeTeamRepositoryImpl: FakeTeamRepositoryImpl): TeamRepository

}