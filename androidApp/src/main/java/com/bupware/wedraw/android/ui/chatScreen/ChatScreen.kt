package com.bupware.wedraw.android.ui.chatScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.bupware.wedraw.android.R
import com.bupware.wedraw.android.ui.drawingScreen.DrawingScreenViewModel
import com.bupware.wedraw.android.ui.drawingScreen.baba
import com.bupware.wedraw.android.ui.drawingScreen.processImage
import io.ak1.drawbox.DrawBox
import io.ak1.drawbox.DrawBoxPayLoad
import io.ak1.drawbox.rememberDrawController

@Preview
@Composable
fun PreviewChatScreen(){
    ChatScreen(rememberNavController())
}

@Composable
fun ChatScreen(navController: NavController){
    ChatScreenBody(navController)
}

@Composable
fun ChatScreenBody(navController: NavController, viewModel: ChatScreenViewModel = hiltViewModel()){

    //Background
    Column(
        Modifier
            .fillMaxSize()
    ) {
        Image(painter = painterResource(R.drawable.mainbackground), contentDescription = "background", contentScale = ContentScale.FillBounds, modifier = Modifier.fillMaxSize())
    }

}