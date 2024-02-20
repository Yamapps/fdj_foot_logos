package com.fdj.footlogos.network.model

import kotlinx.serialization.Serializable

@Serializable
data class AllTeamsDto(
    val teams: List<TeamDto>
)