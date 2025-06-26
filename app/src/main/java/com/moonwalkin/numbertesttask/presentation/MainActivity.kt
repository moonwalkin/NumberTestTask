package com.moonwalkin.numbertesttask.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.moonwalkin.numbertesttask.R
import com.moonwalkin.numbertesttask.navigation.Navigation
import com.moonwalkin.numbertesttask.ui.theme.NumberTestTaskTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var networkMonitor: NetworkMonitor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val snackbarHostState = remember { SnackbarHostState() }
            val isOffline by rememberNetworkMonitor(networkMonitor).collectAsStateWithLifecycle()

            NumberTestTaskTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = {
                        SnackbarHost(
                            hostState = snackbarHostState
                        )
                    }
                ) { innerPadding ->
                    val notConnectedMessage = stringResource(R.string.not_connected)
                    LaunchedEffect(isOffline) {
                        if (isOffline) {
                            snackbarHostState.showSnackbar(
                                message = notConnectedMessage,
                                duration = SnackbarDuration.Indefinite
                            )
                        }
                    }

                    Navigation(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        isOffline = isOffline
                    )
                }
            }
        }
    }
}