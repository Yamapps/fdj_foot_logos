package com.fdj.footlogos.network.model

import kotlinx.serialization.Serializable

@Serializable
data class LeagueDto(
    val idLeague: String,
    val strLeague: String,
    val strSport: String,
    val strLeagueAlternate: String?
)