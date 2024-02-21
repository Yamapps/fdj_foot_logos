package com.fdj.footlogos.data

import com.fdj.footlogos.data.model.League

interface LeagueRepository {
    suspend fun getAllLeagues(): Result<List<League>>
}