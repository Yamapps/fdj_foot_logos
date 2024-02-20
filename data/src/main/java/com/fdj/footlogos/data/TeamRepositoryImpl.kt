package com.fdj.footlogos.data

import com.fdj.footlogos.data.model.Team
import com.fdj.footlogos.data.model.mapTeam
import com.fdj.footlogos.network.NetworkDataSource
import javax.inject.Inject

internal class TeamRepositoryImpl @Inject constructor(
    private val networkDataSource: NetworkDataSource
): TeamRepository{

    override suspend fun getTeamsByLeague(league: String): Result<List<Team>> = networkDataSource.getTeamsByLeague(league)
        .mapCatching {
            it.map { leagueDto ->
                leagueDto.mapTeam()
            }
        }
}