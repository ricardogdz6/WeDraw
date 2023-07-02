package com.bupware.wedraw.android.ui.mainscreen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.bupware.wedraw.android.R
import com.bupware.wedraw.android.components.animations.ChipPop
import com.bupware.wedraw.android.components.systembar.SystemBarColor
import com.checkinapp.ui.theme.blueWeDraw
import com.checkinapp.ui.theme.Lexend
import com.checkinapp.ui.theme.greenWeDraw
import com.checkinapp.ui.theme.redWeDraw
import com.checkinapp.ui.theme.yellowWeDraw
import kotlinx.coroutines.delay

@Composable
@Preview
fun PreviewMain(){
    MainScreen(rememberNavController())
}

@Composable
fun MainScreen(navController: NavController,viewModel: MainViewModel = hiltViewModel()){

    SystemBarColor(color = Color(0xFF2C4560))

    MainScreenBody(navController = navController)
}

@Composable
fun MainScreenBody(navController: NavController, viewModel: MainViewModel = hiltViewModel()){

    //region Image background
    Column(
        Modifier
            .fillMaxSize()
            .background(Color.Red)) {

        Image(painter = painterResource(R.drawable.mainbackground), contentDescription = "background", contentScale = ContentScale.FillBounds, modifier = Modifier.fillMaxSize())
    }
    //endregion

    //Fondo de los backgrounds
    Box {
        GroupBackground(navController)
        UpperBackgroundContent()
    }


    //Boton +
    Column(
        Modifier
            .fillMaxSize()
            .padding(bottom = 30.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Bottom) {
        Button(colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF3D6DA2)),onClick = { viewModel.moreOptionsEnabled = !viewModel.moreOptionsEnabled }, shape = CircleShape) {
            Text(modifier = Modifier.padding(start = 8.dp, end = 8.dp),text = "+", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 40.sp)
        }
    }

    //Logo
    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Image(
            painter = painterResource(R.drawable.logo),
            contentDescription = "background",
            modifier = Modifier
                .size(250.dp)
                .offset(y = (-40).dp)
        )
    }

    //Settings & Notifications
    Row(Modifier.fillMaxWidth()) {

        //Settings
        IconButton(modifier = Modifier.padding(top = 10.dp, start = 15.dp),onClick = {

        }) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.settings),
                tint = Color.White,
                contentDescription = "Settings"
            )
        }

    }

}

//region Background && Content
@Composable
fun GroupBackground(navController: NavController,viewModel: MainViewModel = hiltViewModel()){
    Column(
        Modifier
            .fillMaxSize()
            .padding(top = 90.dp, bottom = 60.dp, start = 20.dp, end = 20.dp)
            .background(Color.Black.copy(0.4f), RoundedCornerShape(15.dp))) {
    }
}

@Composable
fun UpperBackgroundContent(viewModel: MainViewModel = hiltViewModel()) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(top = 90.dp, bottom = 60.dp)
    ) {
        when (viewModel.moreOptionsEnabled) {
            true -> SettingsContent()
            false -> GroupContent()
        }
    }
}
//endregion


//region Groups

@Composable
fun GroupContent(viewModel: MainViewModel = hiltViewModel()){

    //TODO quitar este hardcode
    LaunchedEffect(Unit){
        viewModel.showGroups = true
    }

    var animationLoading = true

    //TODO quitar el hardcodeo de esto
    LazyColumn(modifier = Modifier.fillMaxWidth(),contentPadding = PaddingValues(top = 70.dp, bottom = 10.dp), horizontalAlignment = Alignment.CenterHorizontally){
        items(10) { index ->

            //region delay Incremental
            var visible by remember { mutableStateOf(false) }

            if (animationLoading) {
                LaunchedEffect(viewModel.showGroups) {
                    delay((index + 1) * 100L) // Retraso incremental para cada elemento
                    visible = true
                    animationLoading = false
                }
            } else visible = true
            //endregion

            ChipPop(content = { GroupBar(index) }, show = visible)
            Spacer(modifier = Modifier.height(25.dp))
        }
    }
}

@Composable
fun GroupBar(index: Int) {

    val colors = listOf<Color>(blueWeDraw, greenWeDraw, yellowWeDraw, redWeDraw)
    val selectedColor = colors.random()

    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        //Esta row es el color de abajo
        Box() {

            Row(
                Modifier
                    .height(70.dp)
                    .fillMaxWidth(0.85f)
                    .background(selectedColor, RoundedCornerShape(10.dp))
            ) {
                Text(text = "")
            }

            Row(
                Modifier
                    .height(60.dp)
                    .fillMaxWidth(0.85f)
                    .background(Color.White, RoundedCornerShape(10.dp)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                //TODO controlar que no se desborde el text este y poner ...
                Text(
                    modifier = Modifier.padding(start = 10.dp),
                    text = "$index",
                    fontSize = 20.sp,
                    fontFamily = Lexend
                )
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 10.dp, end = 5.dp),
                    text = "$index",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.End,
                    fontFamily = Lexend
                )

                Box(Modifier.padding(end = 10.dp)) {
                    Box(
                        Modifier
                            .background(selectedColor, RoundedCornerShape(10.dp))
                            .height(IntrinsicSize.Max)
                            .width(IntrinsicSize.Max), contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            modifier = Modifier,
                            imageVector = ImageVector.vectorResource(id = R.drawable.notification),
                            tint = Color.White,
                            contentDescription = "People in group"
                        )
                    }
                }

            }
        }
    }

}

//endregion

//region Settings content
@Composable
fun SettingsContent(viewModel: MainViewModel = hiltViewModel()){

    //TODO quitar este hardcode
    LaunchedEffect(Unit){
        viewModel.showSettings = true
    }

    var buttonDelay by remember { mutableStateOf(false) }
    var buttonDelay2 by remember { mutableStateOf(false) }

    LaunchedEffect(Unit){
        delay(100)
        buttonDelay = true
        delay(100)
        buttonDelay2 = true
    }

    LazyColumn(modifier = Modifier.fillMaxWidth(),contentPadding = PaddingValues(top = 70.dp, bottom = 10.dp), horizontalAlignment = Alignment.CenterHorizontally){
        item {
            ChipPop(content = { CreateGroupButton() }, show = buttonDelay)
            Spacer(modifier = Modifier.height(25.dp))
            ChipPop(content = { JoinGroupButton() }, show = buttonDelay2)
            Spacer(modifier = Modifier.height(25.dp))
        }
    }

}

@Composable
fun ButtonSettings(dataHolder:SettingsButtonFunctionality){

    val colors = listOf<Color>(blueWeDraw, greenWeDraw, yellowWeDraw, redWeDraw)
    val selectedColor = colors.random()

    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Box() {
            //Esta row es el color de abajo
            Row(
                Modifier
                    .height(70.dp)
                    .fillMaxWidth(0.85f)
                    .background(selectedColor, RoundedCornerShape(10.dp))
            ) {
                Text(text = "")
            }

            Row(
                Modifier
                    .height(60.dp)
                    .fillMaxWidth(0.85f)
                    .background(Color.White, RoundedCornerShape(10.dp)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier.padding(start = 10.dp),
                    text = dataHolder.text,
                    fontSize = 20.sp,
                    fontFamily = Lexend,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun CreateGroupButton(){

    val colors = listOf<Color>(blueWeDraw, greenWeDraw, yellowWeDraw, redWeDraw)
    val selectedColor = colors.random()

    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Box(Modifier.clickable { Log.i("wawa","aafwaf") }) {
            //Esta row es el color de abajo
            Row(
                Modifier
                    .height(70.dp)
                    .fillMaxWidth(0.85f)
                    .background(selectedColor, RoundedCornerShape(10.dp))
            ) {
                Text(text = "")
            }

            Row(
                Modifier
                    .height(60.dp)
                    .fillMaxWidth(0.85f)
                    .background(Color.White, RoundedCornerShape(10.dp)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier.padding(start = 10.dp),
                    text = stringResource(R.string.unirse_a_grupo),
                    fontSize = 20.sp,
                    fontFamily = Lexend,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

}

@Composable
fun JoinGroupButton(){

    val colors = listOf<Color>(blueWeDraw, greenWeDraw, yellowWeDraw, redWeDraw)
    val selectedColor = colors.random()

    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Box(Modifier.clickable { Log.i("wawa","aafwaf") }) {
            //Esta row es el color de abajo
            Row(
                Modifier
                    .height(70.dp)
                    .fillMaxWidth(0.85f)
                    .background(selectedColor, RoundedCornerShape(10.dp))
            ) {
                Text(text = "")
            }

            Row(
                Modifier
                    .height(60.dp)
                    .fillMaxWidth(0.85f)
                    .background(Color.White, RoundedCornerShape(10.dp)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier.padding(start = 10.dp),
                    text = stringResource(R.string.crear_grupo),
                    fontSize = 20.sp,
                    fontFamily = Lexend,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

}

data class SettingsButtonFunctionality(
    val text: String,
    val action: () -> Unit
)
//endregion

