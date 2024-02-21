package com.fdj.footlogos.data

import com.fdj.footlogos.data.model.Team

interface TeamRepository {
    suspend fun getTeamsByLeague(league: String): Result<List<Team>>
}