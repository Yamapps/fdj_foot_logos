package com.fdj.footlogos.data.model

import com.fdj.footlogos.network.model.TeamDto
import kotlin.String

data class Team(
    val idTeam: String,
    val strTeamBadge: String
)

fun TeamDto.mapTeam(): Team = Team(idTeam = idTeam, strTeamBadge = strTeamBadge)