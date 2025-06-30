package com.moonwalkin.numbertesttask.presentation.root

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.moonwalkin.numbertesttask.presentation.details.NumberDetailsScreen
import com.moonwalkin.numbertesttask.presentation.home.HomeScreen

@Composable
fun RootContent(
    component: RootComponent,
    isOffline: Boolean,
    modifier: Modifier = Modifier
) {
    Children(
        stack = component.stack,
        modifier = modifier,
        animation = stackAnimation(fade()),
    ) {
        when (val child = it.instance) {
            is RootComponent.Child.HomeChild -> HomeScreen(
                component = child.component,
                isOffline = isOffline
            )

            is RootComponent.Child.DetailsChild -> NumberDetailsScreen(
                number = child.component.state.number,
                numberInfo = child.component.state.text,
                onBackPress = child.component::onBackPressed,
                modifier = Modifier
                    .fillMaxSize()
            )
        }
    }
}