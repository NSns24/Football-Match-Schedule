package com.ns.footballmatchschedule.adapter

import android.graphics.Typeface
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.ns.footballmatchschedule.R
import com.ns.footballmatchschedule.model.Favorite
import com.ns.footballmatchschedule.util.toDate
import org.jetbrains.anko.*
import org.jetbrains.anko.cardview.v7.cardView
import java.text.SimpleDateFormat
import java.util.*

class FavoriteAdapter(private val favorites: List<Favorite>, private val listener: (Favorite) -> Unit) : RecyclerView.Adapter<FavoriteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val holder = FavoriteViewHolder(FavoriteUI().createView(AnkoContext.create(parent.context, parent)))

        holder.itemView.setOnClickListener {
            val pos = holder.adapterPosition
            if(pos != RecyclerView.NO_POSITION) {
                listener(favorites[pos])
            }
        }

        return holder
    }

    override fun getItemCount(): Int = favorites.size

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bindItem(favorites[position])
    }

}

class FavoriteUI : AnkoComponent<ViewGroup> {

    override fun createView(ui: AnkoContext<ViewGroup>): View {
        return with(ui) {
            linearLayout {
                lparams(matchParent, wrapContent)
                orientation = LinearLayout.VERTICAL
                gravity = Gravity.CENTER_HORIZONTAL

                cardView {
                    setContentPadding(0, dip(10), 0, 0)

                    textView {
                        id = R.id.text_date
                        textAlignment = View.TEXT_ALIGNMENT_CENTER
                        textSize = 18f
                        typeface = Typeface.SERIF
                        textColor = ContextCompat.getColor(context, R.color.colorYelOra)
                    }.lparams(matchParent, wrapContent)

                    linearLayout {
                        lparams(matchParent, wrapContent)
                        orientation = LinearLayout.HORIZONTAL
                        gravity = Gravity.CENTER_HORIZONTAL
                        topPadding = dip(15)

                        textView {
                            id = R.id.text_team_home
                            textSize = 18f
                            singleLine = true
                            gravity = Gravity.RIGHT
                            ellipsize = TextUtils.TruncateAt.END
                            typeface = Typeface.SERIF
                        }.lparams(dip(100), wrapContent)

                        textView {
                            id = R.id.text_home_score
                            textSize = 20f
                            padding = dip(15)
                            setTypeface(Typeface.SERIF, Typeface.BOLD)
                        }.lparams(wrapContent, wrapContent)

                        textView {
                            textResource = R.string.vs
                            textSize = 18f
                            typeface = Typeface.SERIF
                        }.lparams(wrapContent, wrapContent)

                        textView {
                            id = R.id.text_away_score
                            textSize = 20f
                            padding = dip(15)
                            setTypeface(Typeface.SERIF, Typeface.BOLD)
                        }.lparams(wrapContent, wrapContent)

                        textView {
                            id = R.id.text_team_away
                            textSize = 18f
                            singleLine = true
                            ellipsize = TextUtils.TruncateAt.END
                            typeface = Typeface.SERIF
                        }.lparams(dip(100), wrapContent)
                    }
                }.lparams(matchParent, wrapContent) {
                    setMargins(dip(20), dip(20), dip(20), 0)
                }
            }
        }
    }

}

class FavoriteViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val matchDate: TextView = view.find(R.id.text_date)
    private val teamHome: TextView = view.find(R.id.text_team_home)
    private val teamAway: TextView = view.find(R.id.text_team_away)
    private val homeScore: TextView = view.find(R.id.text_home_score)
    private val awayScore: TextView = view.find(R.id.text_away_score)

    fun bindItem(favorite: Favorite) {
        matchDate.text = SimpleDateFormat("E, dd MMM yyyy", Locale.getDefault()).format(
            toDate("yyyy-MM-dd", favorite.eventDate)
        )

        teamHome.text = favorite.homeTeam
        teamAway.text = favorite.awayTeam
        homeScore.text = favorite.homeScore
        awayScore.text = favorite.awayScore
    }

}