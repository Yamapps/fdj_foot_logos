package com.fdj.footlogos.network

import com.fdj.footlogos.network.model.AllLeaguesDto
import com.fdj.footlogos.network.model.AllTeamsDto
import com.fdj.footlogos.network.model.LeagueDto
import com.fdj.footlogos.network.model.TeamDto
import kotlinx.coroutines.flow.Flow
import java.lang.Exception

interface NetworkDataSource {
    suspend fun getAllLeagues(): Result<List<LeagueDto>>
    suspend fun getTeamsByLeague(league: String): Result<List<TeamDto>>
}