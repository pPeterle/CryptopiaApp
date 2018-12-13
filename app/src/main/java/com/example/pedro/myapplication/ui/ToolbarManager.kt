package com.example.pedro.myapplication.ui

import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.example.pedro.myapplication.R

class ToolbarManager(
    private var builder: FragmentToolbar,
    private var container: View
) {

    fun prepareToolbar() {
        if (builder.resId != FragmentToolbar.NO_TOOLBAR) {
            val fragmentToolbar = container.findViewById(builder.resId) as Toolbar

            if (builder.title != -1) {
                container.findViewById<TextView>(R.id.toolbar_fragment_title).text = container.resources.getString(builder.title)
            }

            if (builder.menuId != -1) {
                fragmentToolbar.inflateMenu(builder.menuId)
            }

            if (!builder.menuItems.isEmpty() && !builder.menuClicks.isEmpty()) {
                val menu = fragmentToolbar.menu
                for ((index, menuItemId) in builder.menuItems.withIndex()) {
                    (menu.findItem(menuItemId) as MenuItem).setOnMenuItemClickListener(builder.menuClicks[index])
                }
            }

            if (builder.onSearchQueryChange != null && builder.searchView != -1) {
                val menu = fragmentToolbar.menu
                val menuItem = menu.findItem(builder.searchView)

                val searchView = menuItem.actionView as SearchView

                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        searchView.clearFocus()
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        builder.onSearchQueryChange?.onQueryTextChange(newText)
                        return true
                    }

                })

            }
        }
    }
}