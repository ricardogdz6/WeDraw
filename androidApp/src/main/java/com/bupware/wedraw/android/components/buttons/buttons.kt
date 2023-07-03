package com.bupware.wedraw.android.components.buttons

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseInOutBack
import androidx.compose.animation.core.EaseInOutBounce
import androidx.compose.animation.core.EaseInOutElastic
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bupware.wedraw.android.R
import com.bupware.wedraw.android.components.textfields.TextFieldJoin
import com.bupware.wedraw.android.ui.mainscreen.MainViewModel
import com.checkinapp.ui.theme.Lexend
import com.checkinapp.ui.theme.blueVariant2WeDraw
import com.checkinapp.ui.theme.blueVariantWeDraw
import com.checkinapp.ui.theme.blueWeDraw
import com.checkinapp.ui.theme.greenWeDraw
import com.checkinapp.ui.theme.redWeDraw
import com.checkinapp.ui.theme.yellowWeDraw

//region MainScreen
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CreateGroupButton(viewModel: MainViewModel = hiltViewModel()){

    //val colors = listOf<Color>(blueWeDraw, greenWeDraw, yellowWeDraw, redWeDraw)
    val selectedColor = blueWeDraw

    Box(Modifier.fillMaxWidth(0.85f), contentAlignment = Alignment.Center) {
        Box(Modifier.clickable { viewModel.expandButton(1) }) {

            //region Shadow Color
            Column(
                Modifier
                    .background(selectedColor, RoundedCornerShape(10.dp))
                    .fillMaxSize()
            ) {
                Text(text = " ")


                AnimatedContent(targetState = viewModel.expandCreateGroup, transitionSpec = {
                    slideInVertically(
                        animationSpec = tween(300, easing = EaseOut),
                    ) with slideOutVertically(
                        animationSpec = tween(200, easing = EaseOut),
                    )
                }) { state ->


                    Column(
                        Modifier
                            .height(if (state) 320.dp else 50.dp)
                            .fillMaxWidth()

                    ) {
                        Text(text = " ")
                    }


                }
            }

            //endregion
            Column() {

                val targetTextColor by animateColorAsState(
                    targetValue = if (viewModel.expandCreateGroup) Color.White else Color.Black,
                    animationSpec = tween(durationMillis = 500)
                )

                val targetColor by animateColorAsState(
                    targetValue = if (viewModel.expandCreateGroup) blueVariant2WeDraw else Color.White,
                    animationSpec = tween(durationMillis = 500)
                )

                //region UNIRSE A GRUPO LABEL
                Column(
                    Modifier
                        .height(60.dp)
                        .background(Color.White, RoundedCornerShape(10.dp))
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column(
                        Modifier
                            .background(
                                targetColor, RoundedCornerShape(10.dp)
                            )
                            .padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 5.dp),) {
                        Text(
                            color = targetTextColor,
                            text = stringResource(R.string.crear_grupo),
                            fontSize = 20.sp,
                            fontFamily = Lexend,
                            fontWeight = FontWeight.Bold
                        )
                    }

                }
                //endregion

                //region CONTENT EXPANDIDO

                Column(
                    Modifier
                        .offset(y = (-10).dp)
                        .fillMaxWidth()
                        .background(
                            Color.White, RoundedCornerShape(bottomEnd = 10.dp, bottomStart = 10.dp)
                        )) {


                    AnimatedContent(
                        targetState = viewModel.expandCreateGroup,
                        transitionSpec = {
                            slideInVertically(
                                animationSpec = tween(300, easing = EaseOut),
                            ) with
                                    slideOutVertically(
                                        animationSpec = tween(200, easing = EaseOut),
                                    )
                        }
                    ) { state ->

                        Column(
                            Modifier
                                .height(if (state) 260.dp else 0.dp)
                                .fillMaxWidth()
                        ) {

                            Column(
                                Modifier
                                    .padding(top = 10.dp)
                                    .fillMaxWidth()
                                , verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                                Spacer(modifier = Modifier.height(20.dp))
                                Text(text = stringResource(R.string.nombre_del_grupo), fontSize = 20.sp,fontFamily = Lexend, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(10.dp))
                                TextFieldJoin(modificador = Modifier.fillMaxWidth(0.95f))
                                Spacer(modifier = Modifier.height(20.dp))
                                Column(Modifier.padding(start = 10.dp, end = 10.dp)) {
                                    PeopleLimitBar()
                                }
                                Spacer(modifier = Modifier.height(20.dp))
                                JoinGroupSubButton()
                                Spacer(modifier = Modifier.height(10.dp))
                            }

                        }}
                }

                //endregion

            }
        }
    }

}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun JoinGroupButton(viewModel: MainViewModel = hiltViewModel()){

    //TODO Hacer eso random tambien o no?
    //val colors = listOf<Color>(blueWeDraw, greenWeDraw, yellowWeDraw, redWeDraw)
    val selectedColor = redWeDraw
    
    Box(Modifier.fillMaxWidth(0.85f), contentAlignment = Alignment.Center) {
        Box(Modifier.clickable { viewModel.expandButton(2) }) {

            //region Shadow Color
            Column(
                Modifier
                    .background(selectedColor, RoundedCornerShape(10.dp))
                    .fillMaxSize()
            ) {
                Text(text = " ")


                AnimatedContent(targetState = viewModel.expandJoinGroup, transitionSpec = {
                    slideInVertically(
                        animationSpec = tween(300, easing = EaseOut),
                    ) with slideOutVertically(
                        animationSpec = tween(200, easing = EaseOut),
                    )
                }) { state ->


                    Column(
                        Modifier
                            .height(if (state) 260.dp else 50.dp)
                            .fillMaxWidth()

                    ) {
                        Text(text = " ")
                    }


                }
            }

        //endregion
        Column() {

            val targetTextColor by animateColorAsState(
                targetValue = if (viewModel.expandJoinGroup) Color.White else Color.Black,
                animationSpec = tween(durationMillis = 500)
            )

            val targetColor by animateColorAsState(
                targetValue = if (viewModel.expandJoinGroup) blueVariant2WeDraw else Color.White,
                animationSpec = tween(durationMillis = 500)
            )

            //region UNIRSE A GRUPO LABEL
            Column(
                Modifier
                    .height(60.dp)
                    .background(Color.White, RoundedCornerShape(10.dp))
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    Modifier
                        .background(
                            targetColor, RoundedCornerShape(10.dp)
                        )
                        .padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 5.dp),) {
                    Text(
                        color = targetTextColor,
                        text = stringResource(R.string.unirse_a_grupo),
                        fontSize = 20.sp,
                        fontFamily = Lexend,
                        fontWeight = FontWeight.Bold
                    )
                }

            }
            //endregion

            //region CONTENT EXPANDIDO

            Column(
                Modifier
                    .offset(y = (-10).dp)
                    .fillMaxWidth()
                    .background(
                        Color.White, RoundedCornerShape(bottomEnd = 10.dp, bottomStart = 10.dp)
                    )) {


            AnimatedContent(
                targetState = viewModel.expandJoinGroup,
                transitionSpec = {
                    slideInVertically(
                        animationSpec = tween(300, easing = EaseOut),
                    ) with
                            slideOutVertically(
                                animationSpec = tween(200, easing = EaseOut),
                            )
                }
            ) { state ->

                Column(
                    Modifier
                        .height(if (state) 200.dp else 0.dp)
                        .fillMaxWidth()
                        ) {

                    Column(
                        Modifier
                            .padding(top = 10.dp)
                            .fillMaxWidth()
                        , verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(text = stringResource(R.string.c_digo_del_grupo), fontSize = 20.sp,fontFamily = Lexend, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(10.dp))
                        TextFieldJoin(modificador = Modifier.fillMaxWidth(0.95f))
                        Spacer(modifier = Modifier.height(20.dp))
                         JoinGroupSubButton()
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                }}
            }

            //endregion

        }
        }
    }
    
}

@Composable
fun PeopleLimitBar(){
    //TODO quitar el hardcode del limit
    Row(
        Modifier
            .height(40.dp)
            .background(blueVariantWeDraw, RoundedCornerShape(10.dp))
            .padding(start = 10.dp, end = 10.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "${stringResource(R.string.limite_de_personas)} 5", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp, fontFamily = Lexend)
        Spacer(modifier = Modifier.weight(1f))

        Row(Modifier.padding(top = 5.dp, bottom = 5.dp)) {
            PremiumInfoButton()
        }

    }
}

@Composable
fun PremiumInfoButton(){
    Box(
        Modifier
            .clickable { /*TODO METER INFO*/ }
            .background(Color.White, RoundedCornerShape(10.dp))
            .fillMaxHeight().padding(start = 10.dp, end = 10.dp)
            ,contentAlignment = Alignment.Center) {
        Text(text = "?", fontWeight = FontWeight.Bold, fontSize = 20.sp)
    }
}

@Composable
fun JoinGroupSubButton() {

    Box(Modifier
        .clickable { /* TODO JOIN GRUPO */ }
        .width(IntrinsicSize.Max)

    ) {

        Column(
            Modifier
                .background(Color(0xFFAC2525), RoundedCornerShape(15.dp))
                .fillMaxWidth()
                .height(36.dp)
        ) {
            Text(text = " ")
        }

        JoinGroupSubButtonNoShadow()
    }
}
@Composable
fun JoinGroupSubButtonNoShadow(){
    Column(
        Modifier
            .height(IntrinsicSize.Max)
            .width(IntrinsicSize.Max)
            .background(Color(0xFFF53333), RoundedCornerShape(15.dp))
            .padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 5.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = stringResource(R.string.unirse_a_grupo), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp, fontFamily = Lexend)
    }
}

@Composable
fun JoinGroupQRButton(){
    //TODO meter QR button
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

                Column(Modifier.weight(1f)) {

                }

                //TODO cambiar este width por una intrinsic con un minimo
                Column(
                    Modifier
                        .background(Color.Red, RoundedCornerShape(10.dp))
                        .height(IntrinsicSize.Max)
                        .width(40.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        modifier = Modifier,
                        text = "$index",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.End,
                        fontFamily = Lexend
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

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