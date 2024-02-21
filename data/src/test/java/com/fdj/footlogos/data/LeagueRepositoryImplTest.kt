package com.fdj.footlogos.data

import com.fdj.footlogos.common.exception.GenericExceptionCode
import com.fdj.footlogos.common.exception.NetworkException
import com.fdj.footlogos.common.test.TestUtils.getFileContentFromRes
import com.fdj.footlogos.data.model.League
import com.fdj.footlogos.data.model.mapLeague
import com.fdj.footlogos.network.NetworkDataSource
import com.fdj.footlogos.network.model.AllLeaguesDto
import com.fdj.footlogos.network.model.LeagueDto
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class LeagueRepositoryImplTest {


    private val networkDataSource = mockk<NetworkDataSource>()
    private val json = Json { ignoreUnknownKeys = true }

    private lateinit var leagueDtoList: List<LeagueDto>
    private lateinit var leagueList: List<League>

    private lateinit var leagueRepositoryImpl: LeagueRepositoryImpl

    @Before
    fun setup() = runTest {
        val jsonString = getFileContentFromRes("all_leagues.json")
        val response = json.decodeFromString<AllLeaguesDto>(jsonString)
        leagueDtoList = response.leagues
        leagueList = response.leagues.map {
            it.mapLeague()
        }

        leagueRepositoryImpl = LeagueRepositoryImpl(networkDataSource)
    }

    @Test
    fun fetchLeagueList_OK() = runTest {
        coEvery { networkDataSource.getAllLeagues() } returns Result.success(leagueDtoList)

        val result = leagueRepositoryImpl.getAllLeagues()
        assertNotNull(result)
        assertTrue(result.isSuccess)
        assertNotNull(result.getOrNull())
        assertEquals(leagueList, result.getOrNull()!!)
    }

    @Test
    fun fetchLeagueList_Fails() = runTest {
        val exception = NetworkException(GenericExceptionCode.NO_NETWORK_EXCEPTION)
        coEvery { networkDataSource.getAllLeagues() } returns Result.failure(exception)

        val result = leagueRepositoryImpl.getAllLeagues()
        assertNotNull(result)
        assertTrue(result.isFailure)
        assertNotNull(result.exceptionOrNull())
        assertEquals(exception, result.exceptionOrNull()!!)
    }
}