package com.fdj.footlogos.data.model

import com.fdj.footlogos.network.model.LeagueDto

data class League(
    val idLeague: String,
    val strLeague: String,
    val strSport: String,
    val strLeagueAlternate: String?
)

sealed class UiState<out S> {
    data object Loading : UiState<Nothing>()
    data class Success<out S>(val itemList: List<S>) : UiState<S>()
    data class Failure(val throwable: Throwable) : UiState<Nothing>()
}
fun LeagueDto.mapLeague(): League = League(
    idLeague = idLeague,
    strLeague = strLeague,
    strSport = strSport,
    strLeagueAlternate = strLeagueAlternate
)