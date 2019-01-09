package com.cryptopia.android.pPeterle.presentation.mapper

interface Mapper<M, V> {

    fun fromModel(model: M): V

    fun fromBinding(binding: V): M
}