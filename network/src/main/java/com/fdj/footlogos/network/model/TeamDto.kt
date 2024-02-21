package com.fdj.footlogos.network.model

import kotlinx.serialization.Serializable

@Serializable
data class TeamDto(
    val idTeam: String,
    val strTeamBadge: String,
    val strAlternate: String
)