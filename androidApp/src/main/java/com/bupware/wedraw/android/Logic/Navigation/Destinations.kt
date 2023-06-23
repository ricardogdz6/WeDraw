package com.bupware.wedraw.android.Logic.Navigation

sealed class Destinations (val ruta : String) {

    object DrawingScreen: Destinations("DrawingSscreen")

}