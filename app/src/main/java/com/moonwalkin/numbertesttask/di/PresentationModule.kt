package com.moonwalkin.numbertesttask.di

import com.moonwalkin.numbertesttask.presentation.NetworkMonitor
import com.moonwalkin.numbertesttask.presentation.RealNetworkMonitor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
interface PresentationModule {
    @Binds
    fun bindsNetworkMonitor(
        impl: RealNetworkMonitor
    ): NetworkMonitor
}