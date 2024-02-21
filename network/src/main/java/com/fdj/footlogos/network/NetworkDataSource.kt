package com.fdj.footlogos.network

import com.fdj.footlogos.network.model.LeagueDto
import com.fdj.footlogos.network.model.TeamDto

interface NetworkDataSource {
    suspend fun getAllLeagues(): Result<List<LeagueDto>>
    suspend fun getTeamsByLeague(league: String): Result<List<TeamDto>>
}