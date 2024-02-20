package com.fdj.footlogos.network.model

import kotlinx.serialization.Serializable

@Serializable
data class AllLeaguesDto(
    val leagues: List<LeagueDto>
)