package com.fdj.footlogos.league

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fdj.footlogos.common.exception.GenericExceptionCode
import com.fdj.footlogos.data.model.UiState
import com.fdj.footlogos.league.ui.DropDown
import com.fdj.footlogos.league.ui.TeamList
import kotlinx.coroutines.launch


@Composable
fun MainScreen(viewModel: MainViewModel = hiltViewModel()){
    val leaguesUiState by viewModel.leaguesUiState.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()

    var leagueList by remember { mutableStateOf(emptyList<String>()) }
    var selectedOptionText by remember { mutableStateOf("") }

    when(leaguesUiState){
        is UiState.Failure -> {
            RetryCard(
                error = (leaguesUiState as UiState.Failure).throwable.message,
                retryClick = {
                    coroutineScope.launch {
                        viewModel.fetchAllLeagues()
                    }
                })
        }
        UiState.Loading -> {
            Loading()
        }
        is UiState.Success -> {
            leagueList = (leaguesUiState as UiState.Success).itemList.map { it.strLeague }
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .fillMaxSize()
            ) {
                DropDown(
                    items = leagueList,
                    onItemSelected = { selectedOptionText = it },
                    selectedItem = selectedOptionText,
                    label = stringResource(R.string.search_league)
                )

                TeamList(leagueName = selectedOptionText)
            }
        }
    }
}
@Composable
fun RetryCard(
    error: String?,
    retryClick: () -> Unit
){
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
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
    RetryCard(GenericExceptionCode.DATA_NETWORK_EXCEPTION.message){}
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

