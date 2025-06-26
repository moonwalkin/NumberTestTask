package com.moonwalkin.numbertesttask.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.moonwalkin.details.NumberDetailsScreen
import com.moonwalkin.home.HomeScreen
import kotlinx.serialization.Serializable

@Serializable
object Home

@Serializable
data class Details(val number: Long, val info: String)

@Composable
fun Navigation(modifier: Modifier = Modifier, isOffline: Boolean) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Home) {
        composable<Home> {
            HomeScreen(
                modifier = modifier,
                isOffline = isOffline,
                openDetails = { number, info ->
                    navController.navigate(Details(number, info))
                }
            )
        }
        composable<Details> { backStackEntry ->
            val details: Details = backStackEntry.toRoute()
            NumberDetailsScreen(
                modifier = modifier,
                number = details.number,
                numberInfo = details.info,
                onBackPress = navController::popBackStack
            )
        }
    }
}