package com.bupware.wedraw.android

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.bupware.wedraw.Greeting
import com.bupware.wedraw.android.Logic.Navigation.Destinations
import com.bupware.wedraw.android.Logic.Navigation.NavigationHost
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {

                StartingPoint()

            }
        }
    }
}

@Composable
fun StartingPoint(){
    NavigationHost(navController = rememberNavController(), startDestination = Destinations.SplashScreen.ruta )
}


@HiltAndroidApp
class WeDraw: Application(){}

