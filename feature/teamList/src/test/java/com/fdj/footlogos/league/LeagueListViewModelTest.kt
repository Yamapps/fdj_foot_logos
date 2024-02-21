package com.fdj.footlogos.league

import app.cash.turbine.test
import com.fdj.footlogos.common.exception.GenericExceptionCode
import com.fdj.footlogos.common.exception.NetworkException
import com.fdj.footlogos.common.test.TestUtils
import com.fdj.footlogos.data.LeagueRepository
import com.fdj.footlogos.data.TeamRepository
import com.fdj.footlogos.data.model.League
import com.fdj.footlogos.data.model.Team
import com.fdj.footlogos.data.model.UiState
import com.fdj.footlogos.data.model.mapLeague
import com.fdj.footlogos.data.model.mapTeam
import com.fdj.footlogos.network.model.AllLeaguesDto
import com.fdj.footlogos.network.model.AllTeamsDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@OptIn(ExperimentalCoroutinesApi::class)
class LeagueListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val json = Json { ignoreUnknownKeys = true }

    private lateinit var leagueList: List<League>
    private lateinit var teamList: List<Team>

    private val leagueRepository = mockk<LeagueRepository>(relaxed = true)
    private val teamRepository = mockk<TeamRepository>(relaxed = true)

    private lateinit var viewModel: LeagueListViewModel

    @Before
    fun setUp() {
        val jsonStringLeagues = TestUtils.getFileContentFromRes("all_leagues.json")
        val leagueResponse = json.decodeFromString<AllLeaguesDto>(jsonStringLeagues)
        leagueList = leagueResponse.leagues.map {
            it.mapLeague()
        }


        val jsonStringTeams = TestUtils.getFileContentFromRes("french_league.json")
        val teamResponse = json.decodeFromString<AllTeamsDto>(jsonStringTeams)
        teamList = teamResponse.teams.map {
            it.mapTeam()
        }

        viewModel = LeagueListViewModel(leagueRepository, teamRepository)
    }

    @Test
    fun fetchAllLeagues_CalledAtInit()  = runTest {
        coVerify { leagueRepository.getAllLeagues() }
    }

    @Test
    fun fetchAllLeagues_OK()  = runTest {
        coEvery { leagueRepository.getAllLeagues() } returns Result.success(leagueList)

        viewModel.leaguesUiState.test {
            viewModel.fetchAllLeagues()
            //state from initial fetchAllLeagues
            awaitItem()

            //States after new call to fetchAllLeagues
            assertEquals(UiState.Loading, awaitItem())
            assertEquals(UiState.Success(leagueList), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun fetchAllLeagues_Fails()  = runTest {
        val exception = NetworkException(GenericExceptionCode.NO_NETWORK_EXCEPTION)
        coEvery { leagueRepository.getAllLeagues() } returns Result.failure(exception)

        viewModel.leaguesUiState.test {
            viewModel.fetchAllLeagues()
            //state from initial fetchAllLeagues
            awaitItem()

            //States after new call to fetchAllLeagues
            assertEquals(UiState.Loading, awaitItem())
            assertEquals(UiState.Failure(exception), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun fetchAllTeams_OK()  = runTest {
        coEvery { teamRepository.getTeamsByLeague(any()) } returns Result.success(teamList)

        viewModel.teamsUiState.test {
            viewModel.fetchTeamsByLeague("")

            assertEquals(UiState.Loading, awaitItem())
            assertEquals(UiState.Success(teamList), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun fetchAllTeams_Fails()  = runTest {
        val exception = NetworkException(GenericExceptionCode.NO_NETWORK_EXCEPTION)
        coEvery { teamRepository.getTeamsByLeague(any()) } returns Result.failure(exception)

        viewModel.teamsUiState.test {
            viewModel.fetchTeamsByLeague("")

            assertEquals(UiState.Loading, awaitItem())
            assertEquals(UiState.Failure(exception), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    class MainDispatcherRule(
        private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
    ) : TestWatcher() {
        override fun starting(description: Description) {
            Dispatchers.setMain(testDispatcher)
        }

        override fun finished(description: Description) {
            Dispatchers.resetMain()
        }
    }
}
