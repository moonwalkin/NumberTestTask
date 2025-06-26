package com.moonwalkin.numbertesttask.presentation

import kotlinx.coroutines.flow.Flow

interface NetworkMonitor {
    val isConnectionAvailable: Flow<Boolean>
}