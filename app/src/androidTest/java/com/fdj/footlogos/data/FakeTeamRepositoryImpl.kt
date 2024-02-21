package com.fdj.footlogos.data

import com.fdj.footlogos.data.model.League
import com.fdj.footlogos.data.model.Team
import javax.inject.Inject

class FakeTeamRepositoryImpl @Inject constructor(): TeamRepository {

    private val allTeams = mutableMapOf<String, List<Team>>()

    init {
        val leagues = DataProvider.leagues(2)
        leagues.forEach {
            allTeams[it.strLeague] = DataProvider.teams(5)
        }
    }

    override suspend fun getTeamsByLeague(league: String): Result<List<Team>> {
        val teams = allTeams[league]
        return if(teams.isNullOrEmpty()){
            Result.failure(Exception("error message"))
        }
        else{
            Result.success(DataProvider.teams(10))
        }
    }
}