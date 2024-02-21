package com.fdj.footlogos.data

import com.fdj.footlogos.data.model.League
import javax.inject.Inject

class FakeLeagueRepositoryImpl @Inject constructor(): LeagueRepository {

    override suspend fun getAllLeagues(): Result<List<League>> = Result.success(DataProvider.leagues(5))
}