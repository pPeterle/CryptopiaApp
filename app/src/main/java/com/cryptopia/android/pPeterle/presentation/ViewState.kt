package com.cryptopia.android.pPeterle.presentation

typealias ViewStateList<T> = ViewState<List<T>>

sealed class ViewState<T>
class Success<T>(val data: T) : ViewState<T>()
class Failure<T>(val error: Throwable) : ViewState<T>()
class Loading<T> : ViewState<T>()