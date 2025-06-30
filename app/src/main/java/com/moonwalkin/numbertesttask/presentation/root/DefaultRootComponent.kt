package com.moonwalkin.numbertesttask.presentation.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.moonwalkin.numbertesttask.presentation.details.DefaultDetailsComponent
import com.moonwalkin.numbertesttask.presentation.details.DetailsComponent
import com.moonwalkin.numbertesttask.presentation.home.DefaultHomeComponent
import com.moonwalkin.numbertesttask.presentation.home.HomeComponent
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.serialization.Serializable

class DefaultRootComponent @AssistedInject constructor(
    @Assisted("componentContext") private val componentContext: ComponentContext,
    private val detailsFactory: DefaultDetailsComponent.Factory,
    private val homeFactory: DefaultHomeComponent.Factory,
) : RootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, RootComponent.Child>> =
        childStack(
            source = navigation,
            serializer = Config.serializer(),
            initialConfiguration = Config.Home,
            handleBackButton = true,
            childFactory = ::child,
        )

    private fun child(config: Config, componentContext: ComponentContext): RootComponent.Child =
        when (config) {
            is Config.Home -> {
                homeComponent(componentContext).let(RootComponent.Child::HomeChild)
            }

            is Config.Details -> detailsComponent(
                componentContext,
                config
            ).let(RootComponent.Child::DetailsChild)
        }

    private fun homeComponent(componentContext: ComponentContext): HomeComponent =
        homeFactory.create(
            componentContext = componentContext,
            onItemSelected = { id, text ->
                navigation.push(Config.Details(id, text))
            }
        )

    private fun detailsComponent(
        componentContext: ComponentContext,
        config: Config.Details
    ): DetailsComponent =
        detailsFactory.create(
            componentContext = componentContext,
            onBackPressed = { navigation.pop() },
            state = DetailsComponent.State(
                number = config.id,
                text = config.text
            )
        )

    @Serializable
    private sealed interface Config {
        @Serializable
        data object Home : Config

        @Serializable
        data class Details(val id: Long, val text: String) : Config
    }

    @AssistedFactory
    interface Factory {
        fun create(@Assisted("componentContext") componentContext: ComponentContext): DefaultRootComponent
    }
}
