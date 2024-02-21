package com.fdj.footlogos.league.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.fdj.footlogos.data.model.Team
import com.fdj.footlogos.data.model.UiState
import com.fdj.footlogos.league.Loading
import com.fdj.footlogos.league.MainViewModel
import com.fdj.footlogos.league.R
import com.fdj.footlogos.league.RetryCard
import kotlinx.coroutines.launch


@Composable
fun TeamList(
    leagueName: String
){
    if(leagueName.isNotEmpty()){
        TeamListContainer(leagueName = leagueName)
    }
    else{
        EmptyField()
    }
}

@Composable
fun TeamListContainer(
    viewModel: MainViewModel = hiltViewModel(),
    leagueName: String
){
    val coroutineScope = rememberCoroutineScope()
    val teamsUiState by viewModel.teamsUiState.collectAsStateWithLifecycle()

    LaunchedEffect(leagueName){
        viewModel.fetchTeamsByLeague(leagueName)
    }
    when(teamsUiState){
        is UiState.Failure -> {
            RetryCard(
                error = (teamsUiState as UiState.Failure).throwable.message) {
                coroutineScope.launch {
                    viewModel.fetchTeamsByLeague(leagueName)
                }
            }
        }
        UiState.Loading -> {
            Loading()
        }
        is UiState.Success -> {
            val teamList = (teamsUiState as UiState.Success).itemList
            StaggeredTeamList(teamList)
        }
    }
}
@Composable
fun StaggeredTeamList(teamList: List<Team>){
    LazyVerticalStaggeredGrid(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(vertical = 10.dp),
        columns = StaggeredGridCells.Fixed(2)
    ) {
        val count = teamList.size
        items(count = count) { index ->
            val team = teamList[index]
            TeamItem(team = team)
        }
    }
}

@Composable
fun TeamItem(team: Team){
    Column(
        modifier = Modifier
            .wrapContentHeight()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally)
    {
        val placeholder: Painter = painterResource(R.drawable.baseline_missing_24)
        var isLoading by remember { mutableStateOf(true) }
        var isError by remember { mutableStateOf(false) }
        val imageLoader = rememberAsyncImagePainter(
            model = team.strTeamBadge,
            onState = { state ->
                isLoading = state is AsyncImagePainter.State.Loading
                isError = state is AsyncImagePainter.State.Error
            },
        )
        Image(
            painter = if (isError.not()) imageLoader else placeholder,
            contentDescription = stringResource(id = R.string.team_logo_content_description),
            modifier = Modifier
                .size(150.dp)
        )
    }
}

@Preview
@Composable
fun PreviewTeamItem(){
    TeamItem(team = team())
}

@Composable
fun EmptyField(){
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            color = MaterialTheme.colorScheme.primary,
            text = stringResource(id = R.string.please_select_league)
        )
    }
}

@Preview
@Composable
fun PreviewEmptyField(){
    EmptyField()
}


@Preview
@Composable
fun PreviewTeamList(){
    StaggeredTeamList(teamList())
}

fun team() = Team(
    idTeam = "133704",
    strTeamBadge = "https://www.thesportsdb.com/images/media/team/badge/z69be41598797026.png",
    strAlternate = "Stade Brestois 29")

fun teamList() = listOf(team(), team(), team(), team(), team())