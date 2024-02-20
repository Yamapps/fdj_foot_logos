package com.fdj.footlogos.network.retrofit

import com.fdj.footlogos.common.exception.GenericExceptionCode
import com.fdj.footlogos.common.exception.NetworkException
import com.fdj.footlogos.network.NetworkDataSource
import com.fdj.footlogos.network.model.AllLeaguesDto
import com.fdj.footlogos.network.model.AllTeamsDto
import com.fdj.footlogos.network.model.LeagueDto
import com.fdj.footlogos.network.model.TeamDto
import kotlinx.coroutines.runBlocking
import retrofit2.http.GET
import retrofit2.http.Query
import timber.log.Timber
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton

internal interface RetrofitNetworkApi {
    @GET(value = ALL_LEAGUES_SUFFIX)
    suspend fun getAllLeagues(): AllLeaguesDto

    @GET(value = TEAMS_BY_LEAGUE_SUFFIX)
    suspend fun getTeamsByLeague(@Query(PARAM_LEAGUE) league: String): AllTeamsDto
}

@Singleton
internal class RetrofitNetworkDataSource @Inject constructor(
    private val networkApi: RetrofitNetworkApi
): NetworkDataSource {


    private val allLeagues = runBlocking { getAllLeagues()
    }

    @Throws(Exception::class)
    override suspend fun getAllLeagues(): Result<List<LeagueDto>> {
        return try {
            Result.success(networkApi.getAllLeagues().leagues)
        }
        catch (e: NetworkException) {
            Timber.e(e)
            Result.failure(e)
        } catch (e: ConnectException) {
            Timber.e(e)
            Result.failure(NetworkException(GenericExceptionCode.CONNECTION_TIMEOUT_EXCEPTION))
        } catch (e: SocketTimeoutException) {
            Timber.e(e)
            Result.failure(NetworkException(GenericExceptionCode.CONNECTION_TIMEOUT_EXCEPTION))
        } catch (e: UnknownHostException) {
            Timber.e(e)
            Result.failure(NetworkException(GenericExceptionCode.NO_NETWORK_EXCEPTION))
        } catch (e: Exception) {
            Timber.e(e)
            Result.failure(e)
        }
    }

    @Throws(Exception::class)
    override suspend fun getTeamsByLeague(league: String): Result<List<TeamDto>> {
        return try {
            Result.success(networkApi.getTeamsByLeague(league).teams)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

private const val ALL_LEAGUES_SUFFIX = "all_leagues.php"
private const val TEAMS_BY_LEAGUE_SUFFIX = "search_all_teams.php?"

private const val PARAM_LEAGUE = "l"