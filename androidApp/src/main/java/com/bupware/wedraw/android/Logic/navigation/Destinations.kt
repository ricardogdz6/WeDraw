package com.bupware.wedraw.android.Logic.navigation

sealed class Destinations (val ruta : String) {

    object SplashScreen: Destinations("SplashScreen")
    object DrawingScreen: Destinations("DrawingSscreen")

    object LoginScreen:Destinations("LoginScreen")

}