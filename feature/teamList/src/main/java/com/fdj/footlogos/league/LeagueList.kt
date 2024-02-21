package com.fdj.footlogos.league

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fdj.footlogos.common.exception.GenericExceptionCode
import com.fdj.footlogos.data.model.UiState
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeagueList(
    viewModel: LeagueListViewModel = hiltViewModel()
){
    val coroutineScope = rememberCoroutineScope()
    val leagues by viewModel.leaguesUiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Column {
                        Text(
                            text = "Top bar",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        when(leagues){
            is UiState.Failure -> {
                RetryCard(
                    paddingValues = innerPadding,
                    error = (leagues as UiState.Failure).throwable.message) {
                    coroutineScope.launch {
                        viewModel.fetchAllLeagues()
                    }
                }
            }
            UiState.Loading -> {
                Loading()
            }
            is UiState.Success -> {
                val leagueList = (leagues as UiState.Success).leagueList
                LazyColumn(modifier = Modifier
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(5.dp)){
                    val count = leagueList.size
                    items(count = count) { index ->
                        val league = leagueList[index]
                        val color = if(index % 2 == 0){
                            Color.White
                        }
                        else{
                            Color.LightGray
                        }
                        Text(
                            modifier = Modifier
                                .padding(horizontal = 8.dp, vertical = 5.dp)
                                .fillMaxWidth()
                                .background(color)
                                .clickable {
                                    coroutineScope.launch {
                                        viewModel.fetchTeamsByLeague(league.strLeague)
                                    }
                                },
                            text = league.strLeague
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RetryCard(
    error: String?,
    paddingValues: PaddingValues,
    retryClick: () -> Unit
){
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(paddingValues)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer),
            text = error ?: GenericExceptionCode.DATA_NETWORK_EXCEPTION.message,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { retryClick() }) {
            Text(text = stringResource(id = R.string.retry))
        }
    }
}

@Preview
@Composable
fun PreviewRetryCard(){
    RetryCard(GenericExceptionCode.DATA_NETWORK_EXCEPTION.message, PaddingValues(20.dp)){}
}


@Composable
fun Loading() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(modifier = Modifier.size(40.dp))
    }
}


@Preview
@Composable
fun PreviewLoading(){
    Loading()
}