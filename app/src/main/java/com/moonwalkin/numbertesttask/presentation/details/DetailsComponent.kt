package com.moonwalkin.numbertesttask.presentation.details

interface DetailsComponent {

    val state: State

    fun onBackPressed()

    data class State(
        val number: Long,
        val text: String,
    )
}