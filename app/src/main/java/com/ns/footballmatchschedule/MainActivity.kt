package com.ns.footballmatchschedule

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.ns.footballmatchschedule.R.id.*
import com.ns.footballmatchschedule.fragment.FavoriteFragment
import com.ns.footballmatchschedule.fragment.LastMatchFragment
import com.ns.footballmatchschedule.fragment.NextMatchFragment
import org.jetbrains.anko.*
import org.jetbrains.anko.design.*

class MainActivity : AppCompatActivity() {

    private lateinit var frameLayout: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        relativeLayout {
            lparams(matchParent, matchParent)

            frameLayout = frameLayout {
                id = R.id.main_container
            }.lparams(matchParent, matchParent) {
                above(R.id.bottom_layout)
            }

            linearLayout {
                id = R.id.bottom_layout
                orientation = LinearLayout.VERTICAL

                view {
                    background = ContextCompat.getDrawable(context, R.drawable.shadow)
                }.lparams(matchParent, dip(4))

                bottomNavigationView {
                    lparams(matchParent, wrapContent)
                    id = R.id.bottom_navigation
                    inflateMenu(R.menu.bottom_navigation_menu)
                    itemBackground = ContextCompat.getDrawable(context, R.color.colorWhite)
                    itemIconTintList = ContextCompat.getColorStateList(context, R.color.nav_item_color_state)
                    itemTextColor = ContextCompat.getColorStateList(context, R.color.nav_item_color_state)

                    setOnNavigationItemSelectedListener { item ->
                        when(item.itemId) {
                            menu_last_matches -> {
                                loadLastMatchesFragment(savedInstanceState)
                            }

                            menu_next_matches -> {
                                loadNextMatchesFragment(savedInstanceState)
                            }

                            menu_favorites -> {
                                loadFavoriteFragment(savedInstanceState)
                            }
                        }
                        true
                    }

                    selectedItemId = menu_last_matches
                }
            }.lparams(matchParent, wrapContent) {
                alignParentBottom()
            }
        }
    }

    private fun loadLastMatchesFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.main_container, LastMatchFragment(), LastMatchFragment::class.java.simpleName)
                .commit()
        }
    }

    private fun loadNextMatchesFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.main_container, NextMatchFragment(), NextMatchFragment::class.java.simpleName)
                .commit()
        }
    }

    private fun loadFavoriteFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.main_container, FavoriteFragment(), FavoriteFragment::class.java.simpleName)
                .commit()
        }
    }
}
