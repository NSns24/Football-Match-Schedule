package com.ns.footballmatchschedule

import com.ns.footballmatchschedule.model.Match
import com.ns.footballmatchschedule.model.Team

interface MainView {
    fun hideLoading()
    fun showMatches(data: List<Match>)
    fun showMatchDetail(match: Match, home: Team, away: Team)
}