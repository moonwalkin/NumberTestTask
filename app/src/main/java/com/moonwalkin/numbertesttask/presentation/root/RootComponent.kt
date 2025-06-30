package com.moonwalkin.numbertesttask.presentation.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.moonwalkin.numbertesttask.presentation.details.DetailsComponent
import com.moonwalkin.numbertesttask.presentation.home.HomeComponent

interface RootComponent {
    val stack: Value<ChildStack<*, Child>>

    sealed class Child {
        class HomeChild(val component: HomeComponent) : Child()
        class DetailsChild(val component: DetailsComponent) : Child()
    }
}