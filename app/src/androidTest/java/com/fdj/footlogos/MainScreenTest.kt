package com.fdj.footlogos

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.fdj.footlogos.data.LeagueRepository
import com.fdj.footlogos.data.TeamRepository
import com.fdj.footlogos.league.test.TestTags
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class MainScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var leagueRepository: LeagueRepository

    @Inject
    lateinit var teamRepository: TeamRepository

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun initialFieldsDisplayed() = runTest {
        composeTestRule.onNodeWithText("please select a league").assertIsDisplayed()
        composeTestRule.onNodeWithTag(TestTags.SEARCH_FIELD_TAG).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TestTags.CLEAR_BUTTON_TAG).assertIsDisplayed()
    }

    @Test
    fun leaguesDisplayed() = runTest {
        composeTestRule.onNodeWithTag(TestTags.SEARCH_FIELD_TAG).performTextInput("League")

        val leagues = leagueRepository.getAllLeagues().getOrNull()
        assertNotNull(leagues)
        composeTestRule.onNodeWithTag(TestTags.NO_LEAGUE_ITEM_TAG).assertIsNotDisplayed()
        leagues?.forEach {
            composeTestRule.onNodeWithText(it.strLeague).assertIsDisplayed()
        }
    }

    @Test
    fun noLeagueDisplayed() = runTest {
        composeTestRule.onNodeWithTag(TestTags.SEARCH_FIELD_TAG).performTextInput("bla")
        val leagues = leagueRepository.getAllLeagues().getOrNull()
        assertNotNull(leagues)
        composeTestRule.onNodeWithTag(TestTags.NO_LEAGUE_ITEM_TAG).assertIsDisplayed()
        leagues!!.forEach {
            composeTestRule.onNodeWithText(it.strLeague).assertIsNotDisplayed()
        }
    }

    @Test
    fun teamsDisplayed() = runTest {
        val leagueIndex = 0
        composeTestRule.onNodeWithTag(TestTags.SEARCH_FIELD_TAG).performTextInput("league")
        composeTestRule.onAllNodes(hasTestTag(TestTags.LEAGUE_ITEM_TAG)).apply {
            get(leagueIndex).performClick()
        }
        val leagues = leagueRepository.getAllLeagues().getOrNull()!!
        val teams = teamRepository.getTeamsByLeague(leagues[leagueIndex].strLeague).getOrNull()

        assertNotNull(teams)
        val displayedTeamCount = composeTestRule.onAllNodes(hasTestTag(TestTags.TEAM_ITEM_TAG)).fetchSemanticsNodes().size
        val expectedCount = teams!!.size/2 //We only display half the teams
        assertEquals(expectedCount, displayedTeamCount)
    }


    @Test
    fun noTeamsDisplayed_ErrorDisplayed() = runTest {
        val leagueIndex = 3
        composeTestRule.onNodeWithTag(TestTags.SEARCH_FIELD_TAG).performTextInput("league")
        composeTestRule.onAllNodes(hasTestTag(TestTags.LEAGUE_ITEM_TAG)).apply {
            get(leagueIndex).performClick()
        }
        val leagues = leagueRepository.getAllLeagues().getOrNull()!!
        val teamResult = teamRepository.getTeamsByLeague(leagues[leagueIndex].strLeague)

        assertNotNull(teamResult)
        assertTrue(teamResult.isFailure)

        val displayedTeamCount = composeTestRule.onAllNodes(hasTestTag(TestTags.TEAM_ITEM_TAG)).fetchSemanticsNodes().size
        assertEquals(0, displayedTeamCount)

        //Retry view displayed
        composeTestRule.onNodeWithTag(TestTags.RETRY_TAG).assertIsDisplayed()

        //Error message displayed
        val exception = teamResult.exceptionOrNull()
        assertNotNull(exception)
        val expectedErrorMessage = exception?.message!!
        composeTestRule.onNodeWithText(expectedErrorMessage).assertIsDisplayed()
    }
}