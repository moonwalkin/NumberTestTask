package com.moonwalkin.numbertesttask.presentation.home

import com.arkivanov.decompose.value.Value

interface HomeComponent {
    val model: Value<DefaultHomeComponent.HomeScreenState>
    fun loadNumberInfo(number: Long)
    fun loadRandomNumberInfo()
    fun onItemSelected(number: Long, text: String)
}
