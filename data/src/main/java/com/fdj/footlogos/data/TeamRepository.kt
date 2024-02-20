package com.fdj.footlogos.data

import com.fdj.footlogos.data.model.League
import com.fdj.footlogos.data.model.Team
import kotlinx.coroutines.flow.StateFlow

interface TeamRepository {
    suspend fun getTeamsByLeague(league: String): Result<List<Team>>
}