package com.bupware.wedraw.android.UI.DrawingScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Preview
@Composable
fun PreviewDrawingScreen(){
    DrawingScreen(rememberNavController())
}

@Composable
fun DrawingScreen(navController: NavController){
    DrawingScreenBody(navController)
}

@Composable
fun DrawingScreenBody(navController: NavController, viewModel: DrawingScreenViewModel = hiltViewModel()){
    Column(
        Modifier
            .fillMaxSize()
            .background(Color.Red)) {
        Text(text = "aaa")
    }
}