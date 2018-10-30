package com.example.pedro.myapplication

sealed class ViewState<T>
class Success<T>(val data: T) : ViewState<T>()
class Failure<T>(val error: Throwable) : ViewState<T>()
class Loading<T>() : ViewState<T>()