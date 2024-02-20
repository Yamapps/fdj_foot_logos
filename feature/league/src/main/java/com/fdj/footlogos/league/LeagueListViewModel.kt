package com.fdj.footlogos.league

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fdj.footlogos.data.LeagueRepository
import com.fdj.footlogos.data.TeamRepository
import com.fdj.footlogos.data.model.League
import com.fdj.footlogos.data.model.Team
import com.fdj.footlogos.data.model.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LeagueListViewModel @Inject constructor(
    private val leagueRepository: LeagueRepository,
    private val teamRepository: TeamRepository
) : ViewModel() {

    private val _leaguesUiState: MutableStateFlow<UiState<League>> = MutableStateFlow(UiState.Loading)
    val leaguesUiState: StateFlow<UiState<League>> = _leaguesUiState


    private val _teamsUiState: MutableStateFlow<UiState<Team>> = MutableStateFlow(UiState.Loading)
    val teamsUiState: StateFlow<UiState<Team>> = _teamsUiState

    init {
        viewModelScope.launch{
            fetchAllLeagues()
        }
    }

    suspend fun fetchAllLeagues(){
        _leaguesUiState.value = UiState.Loading
        val result = leagueRepository.getAllLeagues()
        try {
            _leaguesUiState.value = UiState.Success(result.getOrThrow())
        } catch (e: Exception){

            UiState.Failure(e)
        }
    }


    suspend fun fetchTeamsByLeague(league: String){
        _teamsUiState.value = UiState.Loading
        val result = teamRepository.getTeamsByLeague(league)

        try {
            val items = result.getOrThrow()
            _teamsUiState.value = UiState.Success(items)
        } catch (e: Exception){
            _teamsUiState.value = UiState.Failure(e)
        }
    }
}