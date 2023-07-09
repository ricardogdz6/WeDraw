package com.bupware.wedraw.android

import android.annotation.SuppressLint
import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.bupware.wedraw.android.components.composables.SnackbarWrapper
import com.bupware.wedraw.android.components.extra.GetDeviceConfig
import com.bupware.wedraw.android.logic.navigation.Destinations
import com.bupware.wedraw.android.logic.navigation.NavigationHost
import com.google.firebase.FirebaseApp
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FirebaseApp.initializeApp(this);

            MyApplicationTheme {

                GetDeviceConfig()

                Scaffold {
                    StartingPoint()

                    SnackbarWrapper()
                }

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



