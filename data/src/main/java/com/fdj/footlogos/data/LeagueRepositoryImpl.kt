package com.fdj.footlogos.data

import com.fdj.footlogos.data.model.League
import com.fdj.footlogos.data.model.mapLeague
import com.fdj.footlogos.network.NetworkDataSource
import javax.inject.Inject

internal class LeagueRepositoryImpl @Inject constructor(
    private val networkDataSource: NetworkDataSource
): LeagueRepository {

    override suspend fun getAllLeagues(): Result<List<League>> = networkDataSource.getAllLeagues()
        .mapCatching {list ->
            list.map { leagueDto ->
                leagueDto.mapLeague()
            }
        }
}