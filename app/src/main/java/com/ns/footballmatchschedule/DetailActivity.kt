package com.ns.footballmatchschedule

import android.database.sqlite.SQLiteConstraintException
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.google.gson.Gson
import com.ns.footballmatchschedule.api.ApiRepository
import com.ns.footballmatchschedule.db.database
import com.ns.footballmatchschedule.model.Favorite
import com.ns.footballmatchschedule.model.Match
import com.ns.footballmatchschedule.model.Team
import com.ns.footballmatchschedule.presenter.MainPresenter
import com.ns.footballmatchschedule.util.invisible
import com.ns.footballmatchschedule.util.toDate
import com.squareup.picasso.Picasso
import org.jetbrains.anko.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jetbrains.anko.design.snackbar
import java.text.SimpleDateFormat
import java.util.*

class DetailActivity : AppCompatActivity(), MainView {

    private lateinit var presenter: MainPresenter
    private lateinit var progressBar: ProgressBar
    private lateinit var matchDetail: Match

    private lateinit var view: RelativeLayout
    private lateinit var content: LinearLayout
    private lateinit var dateMatch: TextView
    private lateinit var homeBadge: ImageView
    private lateinit var awayBadge: ImageView
    private lateinit var homeGoals: TextView
    private lateinit var awayGoals: TextView
    private lateinit var homeDefend: TextView
    private lateinit var awayDefend: TextView
    private lateinit var homeMidfield: TextView
    private lateinit var awayMidfield: TextView
    private lateinit var homeForward: TextView
    private lateinit var awayForward: TextView
    private lateinit var homeSubs: TextView
    private lateinit var awaySubs: TextView
    private lateinit var homeName: TextView
    private lateinit var awayName: TextView
    private lateinit var homeShots: TextView
    private lateinit var awayShots: TextView
    private lateinit var homeScore: TextView
    private lateinit var awayScore: TextView
    private lateinit var homeGoalkeeper: TextView
    private lateinit var awayGoalkeeper: TextView
    private lateinit var homeFormation: TextView
    private lateinit var awayFormation: TextView

    private var menuItem: Menu? = null
    private var isFavorite: Boolean = false
    private lateinit var eventID: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        eventID = intent.getStringExtra("eventID")

        supportActionBar?.title = "Match Detail"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        scrollView {
            view = relativeLayout {
                lparams(matchParent, wrapContent)

                progressBar = progressBar {
                }.lparams {
                    centerHorizontally()
                }

                content = linearLayout {
                    lparams(matchParent, wrapContent)
                    orientation = LinearLayout.VERTICAL
                    padding = dip(20)
                    visibility = View.INVISIBLE

                    dateMatch = textView {
                        textAlignment = View.TEXT_ALIGNMENT_CENTER
                        textSize = 20f
                        textColor = ContextCompat.getColor(context, R.color.colorYelOra)
                    }.lparams(matchParent, wrapContent) {
                        bottomMargin = dip(20)
                    }

                    linearLayout {
                        orientation = LinearLayout.HORIZONTAL
                        gravity = Gravity.CENTER_HORIZONTAL

                        relativeLayout {
                            lparams(dip(100), wrapContent)
                            gravity = Gravity.LEFT

                            homeBadge = imageView {
                                id = R.id.image_home
                            }.lparams(dip(60), dip(60))

                            homeName = textView {
                                id = R.id.text_team_home
                                textAlignment = View.TEXT_ALIGNMENT_CENTER
                            }.lparams(dip(60), wrapContent) {
                                below(R.id.image_home)
                            }

                            homeFormation = textView {
                                textAlignment = View.TEXT_ALIGNMENT_CENTER
                            }.lparams(dip(60), wrapContent) {
                                below(R.id.text_team_home)
                            }
                        }

                        homeScore = textView {
                            textAlignment = View.TEXT_ALIGNMENT_TEXT_START
                            textSize = 20f
                            setTypeface(Typeface.SERIF, Typeface.BOLD)
                        }.lparams(dip(45), wrapContent)

                        textView {
                            textResource = R.string.vs
                            textSize = 20f
                        }.lparams(wrapContent, wrapContent)

                        awayScore = textView {
                            textAlignment = View.TEXT_ALIGNMENT_TEXT_END
                            textSize = 20f
                            setTypeface(Typeface.SERIF, Typeface.BOLD)
                        }.lparams(dip(45), wrapContent)

                        relativeLayout {
                            lparams(dip(100), wrapContent)
                            gravity = Gravity.RIGHT

                            awayBadge = imageView {
                                id = R.id.image_away
                            }.lparams(dip(60), dip(60))

                            awayName = textView {
                                id = R.id.text_team_away
                                textAlignment = View.TEXT_ALIGNMENT_CENTER
                            }.lparams(dip(60), wrapContent) {
                                below(R.id.image_away)
                            }

                            awayFormation = textView {
                                textAlignment = View.TEXT_ALIGNMENT_CENTER
                            }.lparams(dip(60), wrapContent) {
                                below(R.id.text_team_away)
                            }
                        }
                    }.lparams(matchParent, wrapContent) {
                        bottomMargin = dip(10)
                    }

                    view {
                        backgroundColor = ContextCompat.getColor(context, R.color.colorPrimaryDark)
                    }.lparams(matchParent, dip(1)) {
                        bottomMargin = dip(10)
                    }

                    linearLayout {
                        orientation = LinearLayout.HORIZONTAL
                        gravity = Gravity.CENTER_HORIZONTAL

                        homeGoals = textView {
                            textAlignment = View.TEXT_ALIGNMENT_TEXT_START
                        }.lparams(dip(100), wrapContent)

                        textView {
                            textResource = R.string.goals
                            textAlignment = View.TEXT_ALIGNMENT_CENTER
                        }.lparams(dip(100), wrapContent)

                        awayGoals = textView {
                            textAlignment = View.TEXT_ALIGNMENT_TEXT_END
                        }.lparams(dip(100), wrapContent)
                    }.lparams(matchParent, wrapContent) {
                        bottomMargin = dip(20)
                    }

                    linearLayout {
                        orientation = LinearLayout.HORIZONTAL
                        gravity = Gravity.CENTER_HORIZONTAL

                        homeShots = textView {
                            textAlignment = View.TEXT_ALIGNMENT_TEXT_START
                        }.lparams(dip(100), wrapContent)

                        textView {
                            textResource = R.string.shots
                            textAlignment = View.TEXT_ALIGNMENT_CENTER
                        }.lparams(dip(100), wrapContent)

                        awayShots = textView {
                            textAlignment = View.TEXT_ALIGNMENT_TEXT_END
                        }.lparams(dip(100), wrapContent)
                    }.lparams(matchParent, wrapContent) {
                        bottomMargin = dip(10)
                    }

                    view {
                        backgroundColor = ContextCompat.getColor(context, R.color.colorPrimaryDark)
                    }.lparams(matchParent, dip(1)) {
                        bottomMargin = dip(10)
                    }

                    textView {
                        textResource = R.string.lineup
                        textAlignment = View.TEXT_ALIGNMENT_CENTER
                    }.lparams(matchParent, wrapContent) {
                        bottomMargin = dip(20)
                    }

                    linearLayout {
                        orientation = LinearLayout.HORIZONTAL
                        gravity = Gravity.CENTER_HORIZONTAL

                        homeGoalkeeper = textView {
                            textAlignment = View.TEXT_ALIGNMENT_TEXT_START
                        }.lparams(dip(100), wrapContent)

                        textView {
                            textResource = R.string.goalkeeper
                            textAlignment = View.TEXT_ALIGNMENT_CENTER
                        }.lparams(dip(100), wrapContent)

                        awayGoalkeeper = textView {
                            textAlignment = View.TEXT_ALIGNMENT_TEXT_END
                        }.lparams(dip(100), wrapContent)
                    }.lparams(matchParent, wrapContent) {
                        bottomMargin = dip(20)
                    }

                    linearLayout {
                        orientation = LinearLayout.HORIZONTAL
                        gravity = Gravity.CENTER_HORIZONTAL

                        homeDefend = textView {
                            textAlignment = View.TEXT_ALIGNMENT_TEXT_START
                        }.lparams(dip(100), wrapContent)

                        textView {
                            textResource = R.string.defend
                            textAlignment = View.TEXT_ALIGNMENT_CENTER
                        }.lparams(dip(100), wrapContent)

                        awayDefend = textView {
                            textAlignment = View.TEXT_ALIGNMENT_TEXT_END
                        }.lparams(dip(100), wrapContent)
                    }.lparams(matchParent, wrapContent) {
                        bottomMargin = dip(20)
                    }

                    linearLayout {
                        orientation = LinearLayout.HORIZONTAL
                        gravity = Gravity.CENTER_HORIZONTAL

                        homeMidfield = textView {
                            textAlignment = View.TEXT_ALIGNMENT_TEXT_START
                        }.lparams(dip(100), wrapContent)

                        textView {
                            textResource = R.string.midfield
                            textAlignment = View.TEXT_ALIGNMENT_CENTER
                        }.lparams(dip(100), wrapContent)

                        awayMidfield = textView {
                            textAlignment = View.TEXT_ALIGNMENT_TEXT_END
                        }.lparams(dip(100), wrapContent)
                    }.lparams(matchParent, wrapContent) {
                        bottomMargin = dip(20)
                    }

                    linearLayout {
                        orientation = LinearLayout.HORIZONTAL
                        gravity = Gravity.CENTER_HORIZONTAL

                        homeForward = textView {
                            textAlignment = View.TEXT_ALIGNMENT_TEXT_START
                        }.lparams(dip(100), wrapContent)

                        textView {
                            textResource = R.string.forward
                            textAlignment = View.TEXT_ALIGNMENT_CENTER
                        }.lparams(dip(100), wrapContent)

                        awayForward = textView {
                            textAlignment = View.TEXT_ALIGNMENT_TEXT_END
                        }.lparams(dip(100), wrapContent)
                    }.lparams(matchParent, wrapContent) {
                        bottomMargin = dip(20)
                    }

                    linearLayout {
                        orientation = LinearLayout.HORIZONTAL
                        gravity = Gravity.CENTER_HORIZONTAL

                        homeSubs = textView {
                            textAlignment = View.TEXT_ALIGNMENT_TEXT_START
                        }.lparams(dip(100), wrapContent)

                        textView {
                            textResource = R.string.substitutes
                            textAlignment = View.TEXT_ALIGNMENT_CENTER
                        }.lparams(dip(100), wrapContent)

                        awaySubs = textView {
                            textAlignment = View.TEXT_ALIGNMENT_TEXT_END
                        }.lparams(dip(100), wrapContent)
                    }.lparams(matchParent, wrapContent) {
                        bottomMargin = dip(20)
                    }
                }
            }
        }

        favoriteState()

        val request = ApiRepository()
        val gson = Gson()
        presenter = MainPresenter(this, request, gson)
        presenter.getEventDetail(eventID)
    }

    override fun showMatches(data: List<Match>) {

    }

    override fun hideLoading() {
        progressBar.invisible()
    }

    override fun showMatchDetail(match: Match, home: Team, away: Team) {
        var listStr: MutableList<String>? = mutableListOf()
        var str: String? = ""

        matchDetail = match

        dateMatch.text = SimpleDateFormat("E, dd MMM yyyy", Locale.getDefault()).format(
            toDate("yyyy-MM-dd", match.dateEvent)
        )

        Picasso.get().load(home.strTeamBadge).into(homeBadge)
        Picasso.get().load(away.strTeamBadge).into(awayBadge)

        homeName.text = match.strHomeTeam
        awayName.text = match.strAwayTeam
        homeShots.text = match.intHomeShots
        awayShots.text = match.intAwayShots
        homeScore.text = match.intHomeScore
        awayScore.text = match.intAwayScore
        homeGoalkeeper.text = match.strHomeLineupGoalkeeper?.trim()?.replace(";", "")
        awayGoalkeeper.text = match.strAwayLineupGoalkeeper?.trim()?.replace(";", "")
        homeFormation.text = match.strHomeFormation
        awayFormation.text = match.strAwayFormation

        listStr = match.strHomeGoalDetails?.split(";")?.toMutableList()
        listStr?.forEach {
            str += (it.trim() + "\n")
        }
        homeGoals.text = str
        str = ""
        listStr?.clear()

        listStr = match.strAwayGoalDetails?.split(";")?.toMutableList()
        listStr?.forEach {
            str += (it.trim() + "\n")
        }
        awayGoals.text = str
        str = ""
        listStr?.clear()

        listStr = match.strHomeLineupDefense?.split(";")?.toMutableList()
        listStr?.forEach {
            if(it.trim() != "") {
                str += (it.trim() + ";" + "\n")
            }
        }
        homeDefend.text = str
        str = ""
        listStr?.clear()

        listStr = match.strAwayLineupDefense?.split(";")?.toMutableList()
        listStr?.forEach {
            if(it.trim() != "") {
                str += (it.trim() + ";" + "\n")
            }
        }
        awayDefend.text = str
        str = ""
        listStr?.clear()

        listStr = match.strHomeLineupMidfield?.split(";")?.toMutableList()
        listStr?.forEach {
            if(it.trim() != "") {
                str += (it.trim() + ";" + "\n")
            }
        }
        homeMidfield.text = str
        str = ""
        listStr?.clear()

        listStr = match.strAwayLineupMidfield?.split(";")?.toMutableList()
        listStr?.forEach {
            if(it.trim() != "") {
                str += (it.trim() + ";" + "\n")
            }
        }
        awayMidfield.text = str
        str = ""
        listStr?.clear()

        listStr = match.strHomeLineupForward?.split(";")?.toMutableList()
        listStr?.forEach {
            if(it.trim() != "") {
                str += (it.trim() + ";" + "\n")
            }
        }
        homeForward.text = str
        str = ""
        listStr?.clear()

        listStr = match.strAwayLineupForward?.split(";")?.toMutableList()
        listStr?.forEach {
            if(it.trim() != "") {
                str += (it.trim() + ";" + "\n")
            }
        }
        awayForward.text = str
        str = ""
        listStr?.clear()

        listStr = match.strHomeLineupSubstitutes?.split(";")?.toMutableList()
        listStr?.forEach {
            if(it.trim() != "") {
                str += (it.trim() + ";" + "\n")
            }
        }
        homeSubs.text = str
        str = ""
        listStr?.clear()

        listStr = match.strAwayLineupSubstitutes?.split(";")?.toMutableList()
        listStr?.forEach {
            if(it.trim() != "") {
                str += (it.trim() + ";" + "\n")
            }
        }
        awaySubs.text = str
        str = ""
        listStr?.clear()

        content.visibility = View.VISIBLE
        menuItem?.getItem(0)?.isVisible = true
        setFavorite()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        menuItem = menu
        menuItem?.getItem(0)?.isVisible = false
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }

            R.id.add_to_favorite -> {
                if(this::matchDetail.isInitialized) {
                    if (isFavorite) {
                        removeFromFavorite()
                    }
                    else {
                        addToFavorite()
                    }

                    isFavorite = !isFavorite
                    setFavorite()
                }

                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun favoriteState() {
        database.use {
            val result = select(Favorite.TABLE_FAVORITE)
                .whereArgs(
                    "(EVENT_ID = {id})",
                    "id" to eventID
                )

            val favorite = result.parseList(classParser<Favorite>())

            if (!favorite.isEmpty()) {
                isFavorite = true
            }
        }
    }

    private fun addToFavorite() {
        try {
            database.use {
                insert(
                    Favorite.TABLE_FAVORITE,
                    Favorite.EVENT_ID to eventID,
                    Favorite.EVENT_DATE to matchDetail.dateEvent,
                    Favorite.HOME_TEAM to matchDetail.strHomeTeam,
                    Favorite.HOME_SCORE to matchDetail.intHomeScore,
                    Favorite.AWAY_TEAM to matchDetail.strAwayTeam,
                    Favorite.AWAY_SCORE to matchDetail.intAwayScore
                )
            }

            view.snackbar("Added to favorite").show()
        }
        catch (e: SQLiteConstraintException) {
            view.snackbar(e.localizedMessage).show()
        }
    }

    private fun removeFromFavorite() {
        try {
            database.use {
                delete(
                    Favorite.TABLE_FAVORITE,
                    "(EVENT_ID = {id})",
                    "id" to eventID
                )
            }

            view.snackbar("Removed from favorite").show()
        }
        catch (e: SQLiteConstraintException) {
            view.snackbar(e.localizedMessage).show()
        }
    }

    private fun setFavorite() {
        if (isFavorite) {
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_added_to_favorites)
        }
        else {
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_add_to_favorites)
        }
    }

}