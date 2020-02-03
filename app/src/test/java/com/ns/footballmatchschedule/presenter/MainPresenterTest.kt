package com.ns.footballmatchschedule.presenter

import com.google.gson.Gson
import com.ns.footballmatchschedule.MainView
import com.ns.footballmatchschedule.api.ApiRepository
import com.ns.footballmatchschedule.api.TheSportDBApi
import com.ns.footballmatchschedule.model.Match
import com.ns.footballmatchschedule.model.MatchResponse
import com.ns.footballmatchschedule.model.Team
import com.ns.footballmatchschedule.model.TeamResponse
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.Test
import org.junit.Before
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class MainPresenterTest {

    @Mock
    private lateinit var view: MainView

    @Mock
    private lateinit var gson: Gson

    @Mock
    private lateinit var apiRepository: ApiRepository

    private lateinit var presenter: MainPresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = MainPresenter(view, apiRepository, gson)
    }

    @Test
    fun getLastMatches() {
        val matches: MutableList<Match> = mutableListOf()
        val response = MatchResponse(matches)
        val leagueID = "4328"

        GlobalScope.launch {
            `when`(
                gson.fromJson(
                    apiRepository.doRequest(TheSportDBApi.getLastMatches(leagueID)).await(),
                    MatchResponse::class.java
                )
            ).thenReturn(response)

            presenter.getLastMatches(leagueID)

            verify(view).hideLoading()
            verify(view).showMatches(matches)
        }
    }

    @Test
    fun getNextMatches() {
        val matches: MutableList<Match> = mutableListOf()
        val response = MatchResponse(matches)
        val leagueID = "4328"

        GlobalScope.launch {
            `when`(
                gson.fromJson(
                    apiRepository.doRequest(TheSportDBApi.getNextMatches(leagueID)).await(),
                    MatchResponse::class.java
                )
            ).thenReturn(response)

            presenter.getNextMatches(leagueID)

            verify(view).hideLoading()
            verify(view).showMatches(matches)
        }
    }

    @Test
    fun getEventDetail() {
        val match: MutableList<Match> = mutableListOf()
        val home: MutableList<Team> = mutableListOf()
        val away: MutableList<Team> = mutableListOf()

        val matchResponse = MatchResponse(match)
        val homeResponse = TeamResponse(home)
        val awayResponse = TeamResponse(away)

        val eventID = "576606"

        GlobalScope.launch {
            `when`(
                gson.fromJson(
                    apiRepository.doRequest(TheSportDBApi.getEventDetail(eventID)).await(),
                    MatchResponse::class.java
                )
            ).thenReturn(matchResponse)

            `when`(
                gson.fromJson(
                    apiRepository.doRequest(TheSportDBApi.getTeamImage(match[0].idHomeTeam)).await(),
                    TeamResponse::class.java
                )
            ).thenReturn(homeResponse)

            `when`(
                gson.fromJson(
                    apiRepository.doRequest(TheSportDBApi.getTeamImage(match[0].idAwayTeam)).await(),
                    TeamResponse::class.java
                )
            ).thenReturn(awayResponse)

            presenter.getEventDetail(eventID)

            verify(view).hideLoading()
            verify(view).showMatchDetail(match[0], home[0], away[0])
        }
    }

}