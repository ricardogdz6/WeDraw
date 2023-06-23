package com.bupware.wedraw.android.Logic.Navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.bupware.wedraw.android.UI.DrawingScreen.DrawingScreen

@Composable
fun NavigationHost (navController: NavHostController,startDestination: String) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    )
    {
        composable(Destinations.DrawingScreen.ruta) {
            DrawingScreen(navController = navController)
        }
    }
}







