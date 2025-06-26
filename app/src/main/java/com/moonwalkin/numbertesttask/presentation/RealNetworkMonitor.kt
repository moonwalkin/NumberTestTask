package com.moonwalkin.numbertesttask.presentation

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkCapabilities.NET_CAPABILITY_VALIDATED
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class RealNetworkMonitor @Inject constructor(
    @ApplicationContext private val context: Context,
) : NetworkMonitor {

    private val connectivityManager = context
        .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    override val isConnectionAvailable: Flow<Boolean> = callbackFlow {
        val callback = object : ConnectivityManager.NetworkCallback() {

            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities,
            ) {
                super.onCapabilitiesChanged(network, networkCapabilities)

                val connected = networkCapabilities.hasCapability(NET_CAPABILITY_VALIDATED)
                trySend(connected)
            }

            override fun onUnavailable() {
                super.onUnavailable()
                trySend(false)
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                trySend(false)
            }

            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                val capabilities = connectivityManager.getNetworkCapabilities(network)
                trySend(capabilities?.hasCapability(NET_CAPABILITY_VALIDATED) == true)
            }
        }

        connectivityManager.registerDefaultNetworkCallback(callback)

        val initialNetwork = connectivityManager.activeNetwork
        if (initialNetwork != null) {
            val capabilities = connectivityManager.getNetworkCapabilities(initialNetwork)
            trySend(capabilities?.hasCapability(NET_CAPABILITY_VALIDATED) == true)
        } else {
            trySend(false)
        }

        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }.distinctUntilChanged()
}

@Composable
fun rememberNetworkMonitor(
    networkMonitor: NetworkMonitor,
): StateFlow<Boolean> {
    val scope = rememberCoroutineScope()
    return remember(networkMonitor) {
        networkMonitor.isConnectionAvailable
            .map(Boolean::not)
            .stateIn(scope, WhileSubscribed(5_000), false)
    }
}