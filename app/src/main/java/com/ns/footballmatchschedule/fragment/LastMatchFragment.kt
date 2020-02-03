package com.ns.footballmatchschedule.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.google.gson.Gson
import com.ns.footballmatchschedule.DetailActivity
import com.ns.footballmatchschedule.MainView
import com.ns.footballmatchschedule.R
import com.ns.footballmatchschedule.R.color.colorAccent
import com.ns.footballmatchschedule.adapter.MainLastMatchAdapter
import com.ns.footballmatchschedule.api.ApiRepository
import com.ns.footballmatchschedule.model.Match
import com.ns.footballmatchschedule.model.Team
import com.ns.footballmatchschedule.presenter.MainPresenter
import com.ns.footballmatchschedule.util.invisible
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.swipeRefreshLayout

class LastMatchFragment : Fragment(), MainView {

    private val LEAGUE_ID: String = "4328"
    private lateinit var listMatch: RecyclerView
    private lateinit var mainAdapter: MainLastMatchAdapter
    private lateinit var presenter: MainPresenter
    private lateinit var progressBar: ProgressBar
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private var matches: MutableList<Match> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        mainAdapter = MainLastMatchAdapter(matches) {
            startActivity<DetailActivity>("eventID" to it.idEvent)
        }

        val request = ApiRepository()
        val gson = Gson()
        presenter = MainPresenter(this, request, gson)
        presenter.getLastMatches(LEAGUE_ID)

        activity?.title = "Last Matches"

        return UI {
            linearLayout {
                lparams(matchParent, wrapContent)

                swipeRefresh = swipeRefreshLayout {
                    setColorSchemeResources(
                        colorAccent,
                        android.R.color.holo_green_light,
                        android.R.color.holo_orange_light,
                        android.R.color.holo_red_light
                    )

                    relativeLayout {
                        lparams (matchParent, wrapContent)

                        listMatch = recyclerView {
                            lparams(matchParent, wrapContent)
                            id = R.id.list_match
                            layoutManager = LinearLayoutManager(context)
                            adapter = mainAdapter
                        }

                        progressBar = progressBar {
                        }.lparams{
                            centerHorizontally()
                        }
                    }
                }
            }

            swipeRefresh.onRefresh {
                presenter.getLastMatches(LEAGUE_ID)
            }
        }.view

    }

    override fun hideLoading() {
        progressBar.invisible()
    }

    override fun showMatches(data: List<Match>) {
        swipeRefresh.isRefreshing = false
        matches.clear()
        matches.addAll(data)
        mainAdapter.notifyDataSetChanged()
    }

    override fun showMatchDetail(match: Match, home: Team, away: Team) {

    }

}
