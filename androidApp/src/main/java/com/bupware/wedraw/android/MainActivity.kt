package com.bupware.wedraw.android

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.rememberNavController
import com.bupware.wedraw.android.logic.firebase.FBAuth
import com.bupware.wedraw.android.logic.navigation.Destinations
import com.bupware.wedraw.android.logic.navigation.NavigationHost
import com.bupware.wedraw.android.logic.retrofit.repository.UserRepository
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FirebaseApp.initializeApp(this);

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



