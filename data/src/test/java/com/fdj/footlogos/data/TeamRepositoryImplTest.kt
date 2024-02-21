package com.fdj.footlogos.data

import com.fdj.footlogos.common.exception.GenericExceptionCode
import com.fdj.footlogos.common.exception.NetworkException
import com.fdj.footlogos.common.test.TestUtils.getFileContentFromRes
import com.fdj.footlogos.data.model.Team
import com.fdj.footlogos.data.model.mapTeam
import com.fdj.footlogos.network.NetworkDataSource
import com.fdj.footlogos.network.model.AllTeamsDto
import com.fdj.footlogos.network.model.TeamDto
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class TeamRepositoryImplTest {

    private val networkDataSource = mockk<NetworkDataSource>()
    private val json = Json { ignoreUnknownKeys = true }

    private lateinit var teamDtoList: List<TeamDto>
    private lateinit var teamList: List<Team>

    private lateinit var teamRepositoryImpl: TeamRepositoryImpl

    @Before
    fun setup()= runTest {
        val jsonString = getFileContentFromRes("french_league.json")
        val response = json.decodeFromString<AllTeamsDto>(jsonString)
        teamDtoList = response.teams
        teamList = response.teams.map {
            it.mapTeam()
        }

        teamRepositoryImpl = TeamRepositoryImpl(networkDataSource)
    }

    @Test
    fun fetchTeamList_OK() = runTest {
        coEvery { networkDataSource.getTeamsByLeague(any()) } returns Result.success(teamDtoList)

        val result = teamRepositoryImpl.getTeamsByLeague("")
        assertNotNull(result)
        assertTrue(result.isSuccess)
        assertNotNull(result.getOrNull())
        assertEquals(teamList, result.getOrNull()!!)
    }


    @Test
    fun fetchTeamList_Fails() = runTest {
        val exception = NetworkException(GenericExceptionCode.NO_NETWORK_EXCEPTION)
        coEvery { networkDataSource.getTeamsByLeague(any()) } returns Result.failure(exception)

        val result = teamRepositoryImpl.getTeamsByLeague("")
        assertNotNull(result)
        assertTrue(result.isFailure)
        assertNotNull(result.exceptionOrNull())
        assertEquals(exception, result.exceptionOrNull()!!)
    }
}