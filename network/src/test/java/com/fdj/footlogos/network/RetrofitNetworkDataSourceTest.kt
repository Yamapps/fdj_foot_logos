package com.fdj.footlogos.network

import com.fdj.footlogos.common.test.TestUtils.getFileContentFromRes
import com.fdj.footlogos.network.model.AllLeaguesDto
import com.fdj.footlogos.network.model.AllTeamsDto
import com.fdj.footlogos.network.retrofit.RetrofitNetworkApi
import com.fdj.footlogos.network.retrofit.RetrofitNetworkDataSource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.net.UnknownHostException

class RetrofitNetworkDataSourceTest {

    private val api = mockk<RetrofitNetworkApi>(relaxed = true)
    private val json = Json { ignoreUnknownKeys = true }

    private lateinit var dataSource: RetrofitNetworkDataSource

    @Before
    fun setUp() {
        dataSource = RetrofitNetworkDataSource(api)
    }

    @Test
    @Throws(Exception::class)
    fun fetchAllLeagues_OK() = runTest {
        val jsonString = getFileContentFromRes("all_leagues.json")
        val allLeaguesDto = json.decodeFromString<AllLeaguesDto>(jsonString)

        coEvery { api.getAllLeagues() } returns allLeaguesDto

        val result = dataSource.getAllLeagues()
        assertNotNull(result)
        assertTrue(result.isSuccess)
        assertNotNull(result.getOrNull())
        assertEquals(allLeaguesDto.leagues, result.getOrNull()!!)
    }

    @Test
    @Throws(Exception::class)
    fun fetchAllLeagues_Fails() = runTest {
        val errorMessage = "error message"
        coEvery { api.getAllLeagues() } throws UnknownHostException(errorMessage)

        val result = dataSource.getAllLeagues()
        assertNotNull(result)
        assertTrue(result.isFailure)
        assertNotNull(result.exceptionOrNull())
    }

    @Test
    @Throws(Exception::class)
    fun fetchTeamsByLeague_OK() = runTest {
        val jsonString = getFileContentFromRes("french_league.json")
        val allTeamsDto = json.decodeFromString<AllTeamsDto>(jsonString)

        coEvery { api.getTeamsByLeague(any()) } returns allTeamsDto

        val result = dataSource.getTeamsByLeague("")
        assertNotNull(result)
        assertTrue(result.isSuccess)
        assertNotNull(result.getOrNull())
        assertEquals(allTeamsDto.teams, result.getOrNull()!!)
    }

    @Test
    @Throws(Exception::class)
    fun fetchTeamsByLeague_Fails() = runTest {
        val errorMessage = "error message"
        coEvery { api.getTeamsByLeague(any()) } throws UnknownHostException(errorMessage)

        val result = dataSource.getTeamsByLeague("")
        assertNotNull(result)
        assertTrue(result.isFailure)
        assertNotNull(result.exceptionOrNull())
    }
}