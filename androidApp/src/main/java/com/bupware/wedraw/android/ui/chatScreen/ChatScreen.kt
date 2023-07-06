package com.bupware.wedraw.android.ui.chatScreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.bupware.wedraw.android.R
import com.bupware.wedraw.android.components.buttons.SendMessageButton
import com.bupware.wedraw.android.components.textfields.TextFieldMessage
import com.bupware.wedraw.android.ui.drawingScreen.DrawingScreenViewModel
import com.bupware.wedraw.android.ui.drawingScreen.baba
import com.bupware.wedraw.android.ui.drawingScreen.processImage
import com.checkinapp.ui.theme.Lexend
import com.checkinapp.ui.theme.blueVariant2WeDraw
import com.checkinapp.ui.theme.blueWeDraw
import com.checkinapp.ui.theme.greenWeDraw
import com.checkinapp.ui.theme.redWeDraw
import com.checkinapp.ui.theme.yellowWeDraw
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

    //Topbar
    Column() {
        ChatTopBar(navController)
        DrawingCanvas()        
    }

    //Footer
    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Bottom) {

        //TODO TRABAJAR MAS ESTO
        AnimatedVisibility(
            visible = !viewModel.switchDrawingStatus,
            enter = slideInVertically{height -> height},
            exit = slideOutVertically{height -> height}
        ) {
            Footer()
        }

        ColorfulLines()
    }

    /*
    AnimatedVisibility(
        visible = viewModel.switchDrawingStatus,
        enter = slideInVertically{height -> -height},
        exit = slideOutVertically{height -> -height}
    ) {
        //DrawingScreen
        Column(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.95f)
                .background(redWeDraw)) {
            Text(text = "aaaa")
        }
    }
     */
}

@Composable
fun Footer(){
    //TODO esto ha de ser expansible segun se escriba mas
    Row(
        Modifier
            .background(Color.White, RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp))
            .padding(10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
        TextFieldMessage()
        Spacer(modifier = Modifier.width(5.dp))
        SendMessageButton()
    }
}

@Composable
fun ColorfulLines(height: Dp = 10.dp){
    Row(Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier
            .weight(1f)
            .background(blueWeDraw)
            .height(height))
        Spacer(modifier = Modifier
            .weight(1f)
            .background(greenWeDraw)
            .height(height))
        Spacer(modifier = Modifier
            .weight(1f)
            .background(yellowWeDraw)
            .height(height))
        Spacer(modifier = Modifier
            .weight(1f)
            .background(redWeDraw)
            .height(height))
    }
}

@Composable
fun DrawingCanvas(viewModel: ChatScreenViewModel = hiltViewModel()){



    Box() {

        //region BAR
        Row(
            Modifier
                .background(redWeDraw)
                .height(55.dp)
                .fillMaxWidth()
                , horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {

        }
        //endregion


        //region MainButton
        //TODO ANIMATE ESTO
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {

            Box(
                Modifier
                    .height(75.dp)
                    .width(90.dp)
                    .padding(5.dp)
                    .background(redWeDraw, RoundedCornerShape(15.dp)), contentAlignment = Alignment.Center) {

                Box(Modifier.background(blueVariant2WeDraw, RoundedCornerShape(10.dp))) {
                    IconButton(modifier = Modifier.padding(top = 0.dp, start = 5.dp),onClick = {
                        viewModel.switchDrawingStatus = !viewModel.switchDrawingStatus
                    }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.draw),
                            tint = Color.White,
                            contentDescription = "draw"
                        )
                    }
                }

            }
        }
        //endregion


        //region Icons
        Row(
            Modifier
                .height(55.dp)
                .fillMaxWidth()
            , horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {

            //region 3 DOTS
            IconButton(modifier = Modifier.padding(top = 0.dp, start = 5.dp),onClick = {
                TODO()
            }) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.threedots),
                    tint = Color.White,
                    contentDescription = "3 dots"
                )
            }
            //endregion

            Spacer(Modifier.weight(1f))

            //region PEOPLE

            Text(
                modifier = Modifier.padding(end = 5.dp),
                text = "2", //TODO DESHARDCODEAR
                fontSize = 30.sp,
                fontFamily = Lexend,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            IconButton(modifier = Modifier.padding(top = 0.dp, end = 5.dp),onClick = {
                TODO()
            }) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.person),
                    tint = Color.White,
                    contentDescription = "people in group"
                )
            }
            //endregion
            //endregion
        }
        //endregion

    }

}

@Composable
fun ChatTopBar(navController: NavController){

    Box() {


    //red background
    Column(
        Modifier
            .fillMaxHeight(0.12f)
            .fillMaxWidth()
            .background(redWeDraw)) {
        Text(text = " ")
    }
    
    Column(
        Modifier
            .fillMaxHeight(0.12f)
            .background(Color.White, RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp))) {
        
        //Back && Name
        Box(Modifier.fillMaxHeight(0.5f)) {

            //Go back
            Row(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(), verticalAlignment = Alignment.CenterVertically) {
                IconButton(modifier = Modifier.padding(top = 0.dp, start = 5.dp),onClick = {
                    navController.popBackStack()
                }) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.backarrow),
                        tint = blueVariant2WeDraw,
                        contentDescription = "GoBack"
                    )
                }
            }

            //Titulo
            //TODO DESHARDCODEAR
            Row(Modifier.fillMaxHeight(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                Text(modifier = Modifier.fillMaxWidth(), text = "GRUPO DE PRUEBA", fontFamily = Lexend, fontSize = 25.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)

            }
        }

        
        //CODE
        Row(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {


            Row(
                Modifier
                    .fillMaxHeight(0.8f)
                    .offset(x = 7.dp)
                    .border(1.dp, blueVariant2WeDraw, shape = RoundedCornerShape(8.dp))
                    .padding(4.dp)) {
                Text(text = "CODIGO: 23FDSJD", color = blueVariant2WeDraw ,fontFamily = Lexend, fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(end = 15.dp))

            }

            Box(modifier = Modifier
                .offset(x = (-7).dp)
                .fillMaxHeight(0.8f)
                .background(color = blueVariant2WeDraw, RoundedCornerShape(8.dp))
                .clickable { TODO() }){
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.clipboard),
                    tint = Color.White,
                    contentDescription = "CopyToClipboard"
                )
            }


        }

    }

    }
}