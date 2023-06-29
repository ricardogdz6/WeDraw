package com.bupware.wedraw.android.logic.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.bupware.wedraw.android.Login.LoginScreen
import com.bupware.wedraw.android.ui.drawingScreen.DrawingScreen
import com.bupware.wedraw.android.ui.splash.SplashScreen

@Composable
fun NavigationHost (navController: NavHostController,startDestination: String) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    )
    {
        composable(Destinations.SplashScreen.ruta){
            SplashScreen(navController)
        }

        composable(Destinations.DrawingScreen.ruta) {
            DrawingScreen(navController = navController)
        }
        
        composable(Destinations.LoginScreen.ruta){
            LoginScreen(navController = navController)
        }
    }
}








