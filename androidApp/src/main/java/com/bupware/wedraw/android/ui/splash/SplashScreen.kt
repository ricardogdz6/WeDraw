package com.bupware.wedraw.android.ui.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.bupware.wedraw.android.logic.navigation.Destinations
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun SplashScreen(navController: NavController){

    SplashScreenBody()

    //NAVIGATE
    if (Firebase.auth.currentUser != null){
        navController.navigate(route = Destinations.MainScreen.ruta)
    } else navController.navigate(route = Destinations.LoginScreen.ruta)

}

@Composable
fun SplashScreenBody(){
    //TODO Hacer SplashScreen
    Column(
        Modifier
            .fillMaxSize()
            .background(Color.Red)) {
        Text(text = "aa")
    }
}