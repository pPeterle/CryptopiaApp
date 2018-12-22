package com.cryptopia.android.pPeterle.ui

import android.support.annotation.IdRes
import android.support.annotation.MenuRes
import android.support.annotation.StringRes
import android.support.v7.widget.SearchView
import android.view.MenuItem

class FragmentToolbar(
    @IdRes val resId: Int,
    @StringRes val title: Int,
    @IdRes val searchView: Int,
    @MenuRes val menuId: Int,
    val menuItems: MutableList<Int>,
    val menuClicks: MutableList<MenuItem.OnMenuItemClickListener?>,
    val onSearchQueryChange: SearchView.OnQueryTextListener?
) {

    companion object {
        @JvmField
        val NO_TOOLBAR = -1
    }

    class Builder {
        private var resId: Int = -1
        private var menuId: Int = -1
        private var title: Int = -1
        private var searchView: Int = -1
        private var menuItems: MutableList<Int> = mutableListOf()
        private var menuClicks: MutableList<MenuItem.OnMenuItemClickListener?> = mutableListOf()
        private var onSearchQueryChange: SearchView.OnQueryTextListener? = null

        fun withId(@IdRes resId: Int) = apply { this.resId = resId }

        fun withTitle(title: Int) = apply { this.title = title }

        fun withMenu(@MenuRes menuId: Int) = apply { this.menuId = menuId }

        fun withMenuItems(menuItems: List<Int>, menuClicks: List<MenuItem.OnMenuItemClickListener>) =
            apply {
                this.menuItems.addAll(menuItems)
                this.menuClicks.addAll(menuClicks)
            }

        fun withSearchView(@IdRes resId: Int, onQueryTextListener: SearchView.OnQueryTextListener) = apply {
            searchView = resId
            onSearchQueryChange = onQueryTextListener
        }

        fun build() = FragmentToolbar(resId, title, searchView, menuId, menuItems, menuClicks, onSearchQueryChange)
    }
}