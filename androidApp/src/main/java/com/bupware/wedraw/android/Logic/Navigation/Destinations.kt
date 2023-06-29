package com.bupware.wedraw.android.Logic.Navigation

sealed class Destinations (val ruta : String) {

    object SplashScreen: Destinations("SplashScreen")
    object DrawingScreen: Destinations("DrawingSscreen")

    object LoginScreen:Destinations("LoginScreen")

}