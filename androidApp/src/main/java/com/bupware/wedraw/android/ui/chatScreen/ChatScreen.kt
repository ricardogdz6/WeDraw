package com.bupware.wedraw.android.ui.chatScreen

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.bupware.wedraw.android.components.buttons.CircleColoredButton
import com.bupware.wedraw.android.components.buttons.DrawButton
import com.bupware.wedraw.android.components.buttons.EraseButton
import com.bupware.wedraw.android.components.buttons.MoreColorsButton
import com.bupware.wedraw.android.components.buttons.RedoButton
import com.bupware.wedraw.android.components.buttons.SendMessageButton
import com.bupware.wedraw.android.components.buttons.SizeButtons
import com.bupware.wedraw.android.components.buttons.SwitchToDrawingButton
import com.bupware.wedraw.android.components.buttons.UndoButton
import com.bupware.wedraw.android.components.composables.ColorfulLines
import com.bupware.wedraw.android.components.composables.MessageBubble
import com.bupware.wedraw.android.components.composables.MessageBubbleHost
import com.bupware.wedraw.android.components.extra.DeviceConfig
import com.bupware.wedraw.android.components.extra.GetDeviceConfig
import com.bupware.wedraw.android.components.textfields.TextFieldMessage
import com.bupware.wedraw.android.logic.dataHandler.DataHandler
import com.bupware.wedraw.android.logic.models.Group
import com.bupware.wedraw.android.theme.Lexend
import com.bupware.wedraw.android.theme.blueVariant2WeDraw
import com.bupware.wedraw.android.theme.greenAchieve
import com.bupware.wedraw.android.theme.redWeDraw
import com.bupware.wedraw.android.theme.redWrong
import com.godaddy.android.colorpicker.HsvColor
import com.godaddy.android.colorpicker.harmony.ColorHarmonyMode
import com.godaddy.android.colorpicker.harmony.HarmonyColorPicker
import io.ak1.drawbox.DrawBox
import io.ak1.drawbox.DrawController
import io.ak1.drawbox.rememberDrawController
import kotlinx.coroutines.launch

@Preview
@Composable
fun PreviewChatScreen(){
    ChatScreen(rememberNavController(), 2)
}


@Composable
fun ChatScreen(navController: NavController, groupId: Long, viewModel: ChatScreenViewModel = hiltViewModel()){

    val context = LocalContext.current

    LaunchedEffect(Unit){
        viewModel.groupId = groupId
        viewModel.loadMessages(groupId,context)
    }

    //region Forzar update de messages tras un push notification
    if (DataHandler.forceMessagesUpdate.value){
        DataHandler.forceMessagesUpdate.value = false
        viewModel.loadMessages(groupId,context)
        //TODO IF NO OFFSET ENTONCES ESTO DE ABAJO
        viewModel.moveLazyToBottom = true
    }
    //endregion

    //Si recibes notificaciones dentro del chat correspondiente se actualiza to el rato a 0 para que no salga en mainscreen
    LaunchedEffect(DataHandler.notificationList[groupId]){
        DataHandler.notificationList[groupId] = 0
    }

    BackHandler() {
        if (viewModel.switchDrawingStatus) {viewModel.switchDrawingStatus = !viewModel.switchDrawingStatus}
        else navController.popBackStack()
    }

    Box {
        //TODO QUITAR ESTA LINEA Y DEJAR EL CASO DE TRUE SOLO, ESTO ESTÁ ASÍ PARA PODER HACER PREVIEW
        val group = if (DataHandler.groupList.firstOrNull {it.id == groupId} != null) DataHandler.groupList.first {it.id == groupId} else Group(id = null, name = "", code = "",userGroups = null)
        ChatScreenBody(navController, group = group)


        if (viewModel.colorWheelShow) MoreColors()
        if (viewModel.sendConfirmation) ConfirmationWindow()
    }

}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ChatScreenBody(navController: NavController, group: Group ,viewModel: ChatScreenViewModel = hiltViewModel()){

    Body()

    TopBar(navController = navController, code = group.code, groupName = group.name, people = group.userGroups?.size ?:0)

}

@Composable
fun TopBar(navController: NavController, code:String,groupName:String,people:Int){
    //Topbar
    Column() {
        ChatTopBar(navController, code = code, groupName = groupName)
        DrawingCanvas(people = people)
    }
}

@Composable
fun Body(viewModel: ChatScreenViewModel = hiltViewModel()) {
    //Background
    Column(
        Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(R.drawable.mainbackground),
            contentDescription = "background",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )
    }

    Chat()

    //Footer
    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Bottom) {

        Spacer(modifier = Modifier.height(10.dp))

        AnimatedVisibility(
            visible = !viewModel.switchDrawingStatus,
            enter = slideInVertically { height -> height },
            exit = slideOutVertically { height -> height }
        ) {
            Footer()
        }

        ColorfulLines()
    }


}

@Composable
fun Footer(viewModel: ChatScreenViewModel = hiltViewModel()){
    Row(
        Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp))
            .padding(10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {

        Column(Modifier.weight(1f)) {
            TextFieldMessage(value = viewModel.writingMessage, onValueChange = {viewModel.writingMessage = it})
        }
        Spacer(modifier = Modifier.width(5.dp))


            SendMessageButton(viewModel::sendMessage)
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun DrawingCanvas(people:Int,viewModel: ChatScreenViewModel = hiltViewModel()){

    Box() {

        Column() {


            AnimatedContent(targetState = viewModel.switchDrawingStatus, transitionSpec = {
                slideInVertically(
                    animationSpec = spring(dampingRatio = 1f, stiffness = Spring.StiffnessLow),
                ) { height -> -height } with slideOutVertically(
                    animationSpec = spring(dampingRatio = 0.5f, stiffness = Spring.StiffnessLow),
                ) { height -> -height }
            }) {state ->

                GetDeviceConfig()
                val heightBar = if (state) (DeviceConfig.heightPercentage(80)) else 55.dp
                val buttonPadding = if (state) (DeviceConfig.heightPercentage(75)) else 0.dp


                //BAR SIN ICONOS
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(Color.Transparent)
                        ) {

                    //region BAR
                    Row(
                        Modifier
                            .background(redWeDraw)
                            .height(heightBar)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        if (state) CanvasContent()

                    }
                    //endregion


                    //region MainButton
                    Column(Modifier.offset(y = buttonPadding)) {
                        SwitchToDrawingButton(action = { viewModel.switchDrawingStatus = !viewModel.switchDrawingStatus}, drawingState = viewModel.switchDrawingStatus)

                    }
                    //endregion



                }


            }
        }

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
                text = people.toString(),
                fontSize = 30.sp,
                fontFamily = Lexend,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            IconButton(modifier = Modifier.padding(top = 0.dp, end = 5.dp, start = 2.dp),onClick = {
                TODO()
            }) {
                Box(
                    Modifier
                        .background(Color.White, RoundedCornerShape(10.dp))
                        .padding(5.dp)) {
                    Icon(
                        modifier = Modifier.size(35.dp),
                        imageVector = ImageVector.vectorResource(id = R.drawable.people),
                        tint = redWeDraw,
                        contentDescription = "people in group"
                    )
                }
            }
            //endregion
            //endregion
        }
        //endregion
    }

}

@Preview
@Composable
fun CanvasContent(viewModel: ChatScreenViewModel = hiltViewModel()){
    val controller = rememberDrawController()
    val context = LocalContext.current

    //Si se acepta la ventana de confirmación se resetea el canva desde aqui
    LaunchedEffect(viewModel.removeCanva){
        viewModel.removeCanva = false
        controller.reset()
        viewModel.drawState = true
        viewModel.eraseState = false
        viewModel.sizeState = 1
    }

    //Si se confirma en la window entonces se exporta
    LaunchedEffect(viewModel.exportDrawing){
        controller.saveBitmap()
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(top = 55.dp)) {
        Text(modifier = Modifier.fillMaxWidth(), fontSize = 20.sp, fontWeight = FontWeight.Bold ,text = stringResource(R.string.env_a_un_dibujo_a_tus_amigos), textAlign = TextAlign.Center ,color = Color.White, fontFamily = Lexend)

        Spacer(modifier = Modifier.height(20.dp))
        
        Column(
            Modifier
                .fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            Box(modifier = Modifier
                .background(Color.White, RoundedCornerShape(15.dp))
                .fillMaxWidth(0.95f)
                .weight(0.70f)
            ) {
                //TODO CAMBIAR CALLBACK
                Column(Modifier.clip(RoundedCornerShape(15.dp))) {
                    DrawBox(drawController = controller, bitmapCallback = viewModel.processDrawing(context = context))
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Box(modifier = Modifier
                .fillMaxWidth(0.95f)
                .weight(0.30f)
            ){
                CanvasBottom(controller)
            }

            //TODO IF EL DIBUJO ESTA PINTADO ENTONCES CONFIRMWINDOW
            Button(onClick = { viewModel.sendConfirmation = true }, colors = ButtonDefaults.buttonColors(backgroundColor = blueVariant2WeDraw)) {
                Text(text = "Enviar", color = Color.White, fontFamily = Lexend, fontSize = 20.sp)
            }

            Spacer(modifier = Modifier.height(40.dp))
        }

        
    }

}

@Composable
fun CanvasBottom(controller: DrawController, viewModel: ChatScreenViewModel = hiltViewModel()){

    //Si se actualiza el color en el color Wheel aqui se actualiza
    LaunchedEffect(viewModel.controllerColor){
        controller.changeColor(viewModel.controllerColor)
    }

    Column(
        Modifier
            .height(130.dp)
            .fillMaxWidth()
            .background(Color(0x812C4560), RoundedCornerShape(15.dp))
            .padding(10.dp)) {

        //Colors
        Row(
            Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(30.dp))
                .height(50.dp)
                .padding(5.dp), horizontalArrangement = Arrangement.Center) {

            viewModel.colorsAvailable.forEach { 
                CircleColoredButton(color = it, controller)
                Spacer(modifier = Modifier.width(5.dp))
            }

            MoreColorsButton()

        }

        //Tools
        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 10.dp), horizontalArrangement = Arrangement.Center) {
            DrawButton(state = viewModel.drawState, action = {controller.changeColor(Color.Black); viewModel.drawState = true; viewModel.eraseState = false})
            Spacer(modifier = Modifier.width(5.dp))
            EraseButton(state = viewModel.eraseState, action = {controller.changeColor(Color.White); viewModel.drawState = false; viewModel.eraseState = true})
            Spacer(modifier = Modifier.width(15.dp))
            SizeButtons(controller = controller)
            Spacer(modifier = Modifier.width(15.dp))
            UndoButton(action = {controller.unDo()})
            Spacer(modifier = Modifier.width(5.dp))
            RedoButton(action = {controller.reDo()})
        }



    }
}

@Composable
fun MoreColors(viewModel: ChatScreenViewModel = hiltViewModel()){

    //Esto es para borrar la animacion del clickable
    val interactionSource = remember { MutableInteractionSource() }

    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

        Box(
            Modifier
                .fillMaxSize()
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) { viewModel.colorWheelShow = false }
                .background(Color.White.copy(0.5f)))

        Column(
            Modifier
                .background(Color.White, RoundedCornerShape(15.dp))
                .padding(5.dp)
                .fillMaxWidth(0.95f), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {

            Column(
                Modifier
                    .fillMaxHeight(0.40f)
                    .fillMaxWidth(0.95f)
                    .background(Color.White, RoundedCornerShape(15.dp))
            ) {

                HarmonyColorPicker(harmonyMode = ColorHarmonyMode.SHADES, onColorChanged = { color: HsvColor -> viewModel.controllerColor = hsvToColor(color.hue,color.saturation,color.value) })
            }

            Button(modifier = Modifier.fillMaxWidth(0.5f),onClick = { viewModel.colorWheelShow = false }, colors = ButtonDefaults.buttonColors(backgroundColor = blueVariant2WeDraw)){
                Text(text = "Aceptar", color = Color.White, fontFamily = Lexend)
            }
        }

    }

}

@Composable
fun ChatTopBar(navController: NavController, groupName:String ,code:String ,viewModel: ChatScreenViewModel = hiltViewModel()){

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
                Text(modifier = Modifier.fillMaxWidth(), text = groupName, fontFamily = Lexend, fontSize = 25.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)

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
                    .offset(x = 7.dp, y = (-5).dp)
                    .border(1.dp, blueVariant2WeDraw, shape = RoundedCornerShape(8.dp))
                    .padding(4.dp)) {
                Text(text = "CODIGO: $code", color = blueVariant2WeDraw ,fontFamily = Lexend, fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(end = 15.dp))

            }

            Box(modifier = Modifier
                .offset(x = (-5).dp, y = (-5).dp)
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

//region CHAT
@Composable
fun Chat(viewModel: ChatScreenViewModel = hiltViewModel()){

    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(viewModel.moveLazyToBottom) {
        viewModel.moveLazyToBottom = false
        scope.launch {
            listState.scrollToItem(viewModel.messageList.size)
        }
    }


    LazyColumn(state = listState,modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(top = 200.dp,bottom = 100.dp),verticalArrangement = Arrangement.Bottom) {
        items(viewModel.messageList.size) { index ->


            val isIndexRestable = index != 0
            val isMessageSenderSameThanLast =
                isIndexRestable && viewModel.messageList[index - 1].senderId == viewModel.messageList[index].senderId
            val isMessageSenderDifferentThanLast =
                isIndexRestable && viewModel.messageList[index - 1].senderId != viewModel.messageList[index].senderId

            when {

                //Si el siguiente mensaje es de la misma persona se pone el bubble sin arrow
                isMessageSenderSameThanLast -> {
                    if (viewModel.messageList[index].senderId == viewModel.userID) {
                        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End) {
                            Spacer(modifier = Modifier.height(5.dp))
                            MessageBubbleHost(viewModel.messageList[index], false)
                        }

                    } else {
                        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
                            Spacer(modifier = Modifier.height(5.dp))
                            MessageBubble(viewModel.messageList[index], false)
                        }
                    }
                }

                //Si el siguiente mensaje es de otra persona se vuelve a poner el mensaje básico normal
                isMessageSenderDifferentThanLast -> {
                    if (viewModel.messageList[index].senderId == viewModel.userID) {
                        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End) {
                            Spacer(modifier = Modifier.height(10.dp))
                            MessageBubbleHost(viewModel.messageList[index], true)
                        }
                    } else {
                        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
                            Spacer(modifier = Modifier.height(10.dp))
                            MessageBubble(viewModel.messageList[index], true)

                        }
                    }
                }

                //Si no, es un mensaje básico
                else -> {
                    if (viewModel.messageList[index].senderId == viewModel.userID) {
                        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End) {
                            MessageBubbleHost(viewModel.messageList[index], true)
                        }
                    } else {
                        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
                            MessageBubble(viewModel.messageList[index], true)
                        }
                    }
                }


            }

        }
    }
}


//endregion

@Composable
fun ConfirmationWindow(viewModel: ChatScreenViewModel = hiltViewModel()){

    val interactionSource = remember { MutableInteractionSource() }
    val context = LocalContext.current

    BackHandler() {
        viewModel.sendConfirmation = false
    }

    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Box(
            Modifier
                .fillMaxSize()
                .background(Color.White.copy(0.4f))
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) { viewModel.sendConfirmation = false })


            Column(
                Modifier
                    .background(Color.White, RoundedCornerShape(15.dp))
                    .fillMaxWidth(0.95f)
                    .height(200.dp)
                    , horizontalAlignment = Alignment.CenterHorizontally) {


                Spacer(modifier = Modifier
                    .height(25.dp)
                    .fillMaxWidth()
                    .background(
                        blueVariant2WeDraw,
                        RoundedCornerShape(topEnd = 15.dp, topStart = 15.dp)
                    ))

                Column(Modifier.weight(1f), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "¿Estás seguro de querer enviar este dibujo?", color = Color.Black, fontFamily = Lexend)

                    Spacer(modifier = Modifier.height(20.dp))

                    Row() {
                        //TODO ENVIAR IMAGEN
                        Button(onClick = {
                            viewModel.sendConfirmation = false
                            viewModel.exportDrawing = true
                            viewModel.removeCanva = true
                            viewModel.switchDrawingStatus = !viewModel.switchDrawingStatus
                                         }
                            , colors = ButtonDefaults.buttonColors(backgroundColor = greenAchieve)) {
                            Text(text = "Confirmar")
                        }

                        Spacer(modifier = Modifier.width(15.dp))

                        Button(onClick = { viewModel.sendConfirmation = false; }, colors = ButtonDefaults.buttonColors(backgroundColor = redWrong)) {
                            Text(text = "Cancelar")
                        }
                    }

                }

                Spacer(modifier = Modifier
                    .height(25.dp)
                    .fillMaxWidth()
                    .background(
                        blueVariant2WeDraw,
                        RoundedCornerShape(bottomEnd = 15.dp, bottomStart = 15.dp)
                    ))

            }

    }
}

