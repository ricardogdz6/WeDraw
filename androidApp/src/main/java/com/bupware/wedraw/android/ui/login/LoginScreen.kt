package com.bupware.wedraw.android.Login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun PreviewLogin(){
    LoginScreen(rememberNavController())
}

@Composable
fun LoginScreen(navController: NavController){
    LoginScreenBody(navController)
}

@Composable
fun LoginScreenBody(navController: NavController){

    Column(Modifier.fillMaxSize().background(Color.Black), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {



    }

}