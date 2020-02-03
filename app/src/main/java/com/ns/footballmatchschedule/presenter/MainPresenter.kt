package com.ns.footballmatchschedule.presenter

import com.google.gson.Gson
import com.ns.footballmatchschedule.MainView
import com.ns.footballmatchschedule.api.ApiRepository
import com.ns.footballmatchschedule.api.TheSportDBApi
import com.ns.footballmatchschedule.model.MatchResponse
import com.ns.footballmatchschedule.model.TeamResponse
import com.ns.footballmatchschedule.util.MainIdlingResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainPresenter(private val view: MainView, private val apiRepository: ApiRepository, private val gson: Gson) {

    fun getLastMatches(leagueID: String?) {

        GlobalScope.launch(Dispatchers.Main) {
            MainIdlingResource.increment()

            val data = gson.fromJson(
                apiRepository.doRequest(TheSportDBApi.getLastMatches(leagueID)).await(),
                MatchResponse::class.java
            )

            MainIdlingResource.decrement()
            view.hideLoading()
            view.showMatches(data.events)
        }

    }

    fun getNextMatches(leagueID: String?) {

        GlobalScope.launch(Dispatchers.Main) {
            MainIdlingResource.increment()

            val data = gson.fromJson(
                apiRepository.doRequest(TheSportDBApi.getNextMatches(leagueID)).await(),
                MatchResponse::class.java
            )

            MainIdlingResource.decrement()
            view.hideLoading()
            view.showMatches(data.events)
        }
    }

    fun getEventDetail(eventID: String?) {

        GlobalScope.launch(Dispatchers.Main) {
            MainIdlingResource.increment()

            val match = gson.fromJson(
                apiRepository.doRequest(TheSportDBApi.getEventDetail(eventID)).await(),
                MatchResponse::class.java
            )

            val home = gson.fromJson(
                apiRepository.doRequest(TheSportDBApi.getTeamImage(match.events[0].idHomeTeam)).await(),
                TeamResponse::class.java
            )

            val away = gson.fromJson(
                apiRepository.doRequest(TheSportDBApi.getTeamImage(match.events[0].idAwayTeam)).await(),
                TeamResponse::class.java
            )

            MainIdlingResource.decrement()
            view.hideLoading()
            view.showMatchDetail(match.events[0], home.teams[0], away.teams[0])
        }
    }

}