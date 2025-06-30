package com.moonwalkin.numbertesttask.presentation.details

import com.arkivanov.decompose.ComponentContext
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class DefaultDetailsComponent @AssistedInject constructor(
    @Assisted("componentContext") private val componentContext: ComponentContext,
    @Assisted("onBackPressed") private val onBackPressed: () -> Unit,
    @Assisted("state") override val state: DetailsComponent.State
) : DetailsComponent, ComponentContext by componentContext {

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("onBackPressed") onBackPressed: () -> Unit,
            @Assisted("componentContext") componentContext: ComponentContext,
            @Assisted("state") state: DetailsComponent.State,
        ): DefaultDetailsComponent
    }

    override fun onBackPressed() {
        onBackPressed.invoke()
    }
}