package com.bupware.wedraw.android.logic.navigation

sealed class Destinations (val ruta : String) {

    object SplashScreen: Destinations("SplashScreen")
    object DrawingScreen: Destinations("DrawingSscreen")
    object LoginScreen:Destinations("LoginScreen")
    object MainScreen:Destinations("MainScreen")

}