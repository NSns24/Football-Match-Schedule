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
import com.ns.footballmatchschedule.R.string.*
import com.ns.footballmatchschedule.R.color.*
import com.ns.footballmatchschedule.R.id.*
import com.ns.footballmatchschedule.model.Match
import com.ns.footballmatchschedule.util.toDate
import org.jetbrains.anko.*
import org.jetbrains.anko.cardview.v7.cardView
import java.text.SimpleDateFormat
import java.util.*

class MainNextMatchAdapter(private val matches: List<Match>, private val listener: (Match) -> Unit) : RecyclerView.Adapter<NextMatchViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NextMatchViewHolder {
        val holder = NextMatchViewHolder(NextMatchUI().createView(AnkoContext.create(parent.context, parent)))

        holder.itemView.setOnClickListener {
            val pos = holder.adapterPosition
            if(pos != RecyclerView.NO_POSITION) {
                listener(matches[pos])
            }
        }

        return holder
    }

    override fun getItemCount(): Int = matches.size

    override fun onBindViewHolder(holder: NextMatchViewHolder, position: Int) {
        holder.bindItem(matches[position])
    }

}

class NextMatchUI : AnkoComponent<ViewGroup> {

    override fun createView(ui: AnkoContext<ViewGroup>): View {
        return with(ui) {
            linearLayout {
                lparams(matchParent, wrapContent)
                orientation = LinearLayout.VERTICAL
                gravity = Gravity.CENTER_HORIZONTAL

                cardView {
                    setContentPadding(0, dip(10), 0, 0)

                    textView {
                        id = text_date
                        textAlignment = View.TEXT_ALIGNMENT_CENTER
                        textSize = 18f
                        typeface = Typeface.SERIF
                        textColor = ContextCompat.getColor(context, colorYelOra)
                    }.lparams(matchParent, wrapContent)

                    linearLayout {
                        lparams(matchParent, wrapContent)
                        orientation = LinearLayout.HORIZONTAL
                        gravity = Gravity.CENTER_HORIZONTAL
                        topPadding = dip(15)

                        textView {
                            id = text_team_home
                            textSize = 18f
                            singleLine = true
                            gravity = Gravity.RIGHT
                            ellipsize = TextUtils.TruncateAt.END
                            typeface = Typeface.SERIF
                            padding = dip(15)
                        }.lparams(dip(100), wrapContent)

                        textView {
                            textResource = vs
                            textSize = 18f
                            typeface = Typeface.SERIF
                            padding = dip(15)
                        }.lparams(wrapContent, wrapContent)

                        textView {
                            id = text_team_away
                            textSize = 18f
                            singleLine = true
                            ellipsize = TextUtils.TruncateAt.END
                            typeface = Typeface.SERIF
                            padding = dip(15)
                        }.lparams(dip(100), wrapContent)
                    }
                }.lparams(matchParent, wrapContent) {
                    setMargins(dip(20), dip(20), dip(20), 0)
                }
            }
        }
    }

}

class NextMatchViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val matchDate: TextView = view.find(text_date)
    private val teamHome: TextView = view.find(text_team_home)
    private val teamAway: TextView = view.find(text_team_away)

    fun bindItem(match: Match) {
        matchDate.text = SimpleDateFormat("E, dd MMM yyyy", Locale.getDefault()).format(
            toDate("yyyy-MM-dd", match.dateEvent)
        )

        teamHome.text = match.strHomeTeam
        teamAway.text = match.strAwayTeam
    }

}