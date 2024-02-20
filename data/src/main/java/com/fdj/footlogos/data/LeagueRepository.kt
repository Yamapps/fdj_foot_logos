package com.fdj.footlogos.data

import com.fdj.footlogos.data.model.League
import kotlinx.coroutines.flow.Flow

interface LeagueRepository {
    suspend fun getAllLeagues(): Result<List<League>>
}