package com.ns.footballmatchschedule.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ns.footballmatchschedule.DetailActivity
import com.ns.footballmatchschedule.R
import com.ns.footballmatchschedule.adapter.FavoriteAdapter
import com.ns.footballmatchschedule.db.database
import com.ns.footballmatchschedule.model.Favorite
import org.jetbrains.anko.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast

class FavoriteFragment : Fragment() {

    private lateinit var listMatch: RecyclerView
    private lateinit var mainAdapter: FavoriteAdapter
    private var favorites: MutableList<Favorite> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        mainAdapter = FavoriteAdapter(favorites) {
            startActivity<DetailActivity>("eventID" to it.eventID)
        }

        activity?.title = "Favorites"

        return UI {
            relativeLayout {
                lparams(matchParent, wrapContent)

                listMatch = recyclerView {
                    lparams(matchParent, wrapContent)
                    id = R.id.list_match
                    layoutManager = LinearLayoutManager(context)
                    adapter = mainAdapter
                }
            }
        }.view
    }

    override fun onResume() {
        super.onResume()
        getFavorites()
    }

    private fun getFavorites() {
        context?.database?.use {
            val result = select(Favorite.TABLE_FAVORITE)
            val favoritesResult = result.parseList(classParser<Favorite>())
            favorites.clear()
            favorites.addAll(favoritesResult)
            mainAdapter.notifyDataSetChanged()

            if(favorites.isEmpty()) {
                toast("NO DATA")
            }
        }
    }

}
