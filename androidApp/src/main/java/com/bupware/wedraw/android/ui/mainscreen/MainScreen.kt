package com.bupware.wedraw.android.ui.mainscreen

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
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
import androidx.compose.ui.platform.LocalContext
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
import com.bupware.wedraw.android.components.buttons.CreateGroupButton
import com.bupware.wedraw.android.components.buttons.GroupBar
import com.bupware.wedraw.android.components.buttons.JoinGroupButton
import com.bupware.wedraw.android.components.composables.ColorfulLines
import com.bupware.wedraw.android.components.systembar.SystemBarColor
import com.bupware.wedraw.android.components.textfields.TextFieldUsername
import com.bupware.wedraw.android.logic.dataHandler.DataHandler
import com.bupware.wedraw.android.logic.models.Group
import com.bupware.wedraw.android.logic.navigation.Destinations
import com.bupware.wedraw.android.theme.Lexend
import com.bupware.wedraw.android.theme.blueVariant2WeDraw
import com.bupware.wedraw.android.theme.blueWeDraw
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay

@Composable
@Preview
fun PreviewMain(){
    MainScreen(rememberNavController())
}

@Composable
fun MainScreen(navController: NavController,viewModel: MainViewModel = hiltViewModel()){

    val context = LocalContext.current

    //region Forzar update de grupos de internet
    if (DataHandler.forceUpdate.value){
        DataHandler.forceUpdate.value = false
        viewModel.groupList = emptyList<Group>().toMutableList()
        Log.i("wawaa", DataHandler.groupList.toString())
        viewModel.groupList = DataHandler.groupList
    }
    //endregion

    LaunchedEffect(Unit){
        viewModel.initValues(context)
    }

    //TODO QUITAR ESTE FIX
    BackHandler() {}

    SystemBarColor(color = Color(0xFF2C4560))

    MainScreenBody(navController = navController)

    if (viewModel.askForUsername) UsernamePopUp()

    if (viewModel.navigateToChat) {viewModel.navigateToChat = false;navController.navigate(route = "${Destinations.ChatScreen.ruta}/${viewModel.targetNavigation}")}
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
        UpperBackgroundContent(navController)
    }


    //Boton +
    Column(
        Modifier
            .fillMaxSize()
            .padding(bottom = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        IconButton(
            onClick = { viewModel.moreOptionsEnabled = !viewModel.moreOptionsEnabled },
            modifier = Modifier
                .size(70.dp)
                .background(
                    color = blueWeDraw,
                    shape = CircleShape
                )

        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.add),
                contentDescription = null,
                tint = Color.White,


            )
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

    //ColorfulLines
    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Bottom) {
        ColorfulLines()
    }



    //TODO ELIMINAR
    Column() {
    Button(onClick = {

        Log.i("wawa", viewModel.groupList.toString())
        Firebase.auth.signOut();

        navController.navigate(Destinations.LoginScreen.ruta) {
            popUpTo(Destinations.MainScreen.ruta) {
                inclusive = true
            }
        }


    }) {
        Text(text = "DESLOGUEAR")
    }

    }



}

@Composable
fun UsernamePopUp(viewModel: MainViewModel = hiltViewModel()){

    val context = LocalContext.current

    Box(contentAlignment = Alignment.Center) {
        Column(
            Modifier
                .fillMaxSize()
                .background(Color(0xD3000000))) {
            Text(text = " ")
        }

        Column(
            Modifier
                .background(Color.Red)
                .fillMaxHeight(0.5f)
                .fillMaxWidth(0.9f), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "ELIGE NUEVO USERNAME")
            TextFieldUsername(viewModel.username,onValueChange = {viewModel.username = it})
            Button(onClick = { if (viewModel.username.isNotBlank()) viewModel.launchUpdateUsername(context) }) {
                Text(text = "VALIDAR")
            }
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
fun UpperBackgroundContent(navController: NavController,viewModel: MainViewModel = hiltViewModel()) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(top = 90.dp, bottom = 60.dp)
    ) {
        when (viewModel.moreOptionsEnabled) {
            true -> SettingsContent()
            false -> GroupContent(navController = navController)
        }

    }
}
//endregion


//region Groups

@Composable
fun GroupContent(viewModel: MainViewModel = hiltViewModel(),navController: NavController){

    if (viewModel.showGroups){

        Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(color = blueVariant2WeDraw)
        }

    }
    else if (viewModel.showGroups && viewModel.groupList.isEmpty()) {

        Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            //TODO ANIMACION VANISH?
            Text(text = stringResource(R.string.crea_o_nete_a_un_grupo), fontFamily = Lexend, fontWeight = FontWeight.Bold)
        }

    }
    else {

        var animationLoading = true

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(top = 70.dp, bottom = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(viewModel.groupList.size) { index ->

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


                Column(Modifier.padding(bottom = 25.dp)) {
                    ChipPop(
                        content = { GroupBar(viewModel.groupList.toList()[index].name, viewModel.groupList.toList()[index].id.toString() ,navController) },
                        show = visible
                    )
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

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(top = 70.dp, bottom = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Column(Modifier.padding(bottom = 25.dp)) {
                ChipPop(content = { CreateGroupButton() }, show = buttonDelay)
            }
            Column(Modifier.padding(bottom = 25.dp)) {
                ChipPop(content = { JoinGroupButton() }, show = buttonDelay2)
            }

        }
    }

}
//endregion

