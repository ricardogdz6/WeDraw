package com.bupware.wedraw.android.components.composables

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bupware.wedraw.android.logic.models.Message
import com.bupware.wedraw.android.ui.chatScreen.obtenerHoraMinuto
import com.checkinapp.ui.theme.blueVariant2WeDraw
import com.checkinapp.ui.theme.redWeDraw

@Composable
fun MessageBubbleHost(message: Message, showTriangle:Boolean){

    val cornerShape = with(LocalDensity.current) { 16.dp.toPx() }
    val arrowWidth = with(LocalDensity.current) { if (showTriangle) 8.dp.toPx() else 0.dp.toPx() }
    val arrowHeight = with(LocalDensity.current) {if (showTriangle) 12.dp.toPx() else 0.dp.toPx()  }

    Column(
        modifier = Modifier
            .width(IntrinsicSize.Max)
            .wrapContentSize()
            .padding(end = 16.dp, start = 30.dp),
        horizontalAlignment = Alignment.End
    ) {

        Surface(elevation = 3.dp, shape =
        if (showTriangle){
            RightBubbleShape(
                cornerShape = cornerShape,
                arrowWidth = arrowWidth,
                arrowHeight = arrowHeight,
                arrow = false
            )} else RoundedCornerShape(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize()
                    .drawRightBubble(
                        arrow = false,
                        cornerShape = cornerShape,
                        arrowWidth = arrowWidth,
                        arrowHeight = arrowHeight,
                        bubbleColor = Color(0xFFB7FF91)
                    )
            ) {

                //TODO METER FECHA?

                Text(
                    text = message.text,
                    modifier = Modifier.padding(top = 8.dp, start = 8.dp, end = 8.dp, bottom = 8.dp),
                    fontSize = 14.sp,
                    color = Color.Black
                )
                /*
                Row() {
                    Box(Modifier.weight(1f)) {
                        Text(
                            text = message.text,
                            modifier = Modifier.padding(top = 8.dp, start = 8.dp, end = 8.dp, bottom = 8.dp),
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                    }

                    Box(Modifier.weight(0.2f).fillMaxHeight().background(Color.Red), contentAlignment = Alignment.BottomCenter) {
                        Text(
                            text = obtenerHoraMinuto(message.date!!),
                            modifier = Modifier.padding(top = 8.dp, start = 8.dp, end = 8.dp, bottom = 8.dp),
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                    }
                }

                 */

            }
        }
    }
}

@Composable
fun MessageBubble(message: Message, showTriangle:Boolean){
    val cornerShape = with(LocalDensity.current) { 16.dp.toPx() }
    val arrowWidth = with(LocalDensity.current) { if (showTriangle) 8.dp.toPx() else 0.dp.toPx() }
    val arrowHeight = with(LocalDensity.current) {if (showTriangle) 12.dp.toPx() else 0.dp.toPx()  }

    Log.i("wawa",message.toString())

    Column(
        modifier = Modifier
            .width(IntrinsicSize.Max)
            .wrapContentSize()
            .graphicsLayer(rotationY = 180f)
            .padding(end = 16.dp, start = 30.dp),
        horizontalAlignment = Alignment.End
    ) {

        Surface(elevation = 3.dp, shape =
        if (showTriangle){
            RightBubbleShape(
                cornerShape = cornerShape,
                arrowWidth = arrowWidth,
                arrowHeight = arrowHeight,
                arrow = false
            )} else RoundedCornerShape(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize()
                    .drawRightBubble(
                        cornerShape = cornerShape,
                        arrowWidth = arrowWidth,
                        arrowHeight = arrowHeight,
                        bubbleColor = Color.White
                    )
            ) {
                Column() {

                    if (showTriangle) {
                        Text(
                            text = message.senderId, //TODO AQUI EL NOMBRE
                            modifier = Modifier
                                .fillMaxWidth()
                                .graphicsLayer(rotationY = 180f)
                                .padding(top = 8.dp, start = 8.dp, end = 8.dp, bottom = 1.dp),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = blueVariant2WeDraw //TODO CAMBIAL.
                            , textAlign = TextAlign.Start
                        )
                    }

                    Text(
                        text = message.text,
                        modifier = Modifier
                            .graphicsLayer(rotationY = 180f)
                            .padding(
                                top = if (showTriangle) {
                                    0.dp
                                } else 8.dp, start = 8.dp, end = 8.dp, bottom = 8.dp
                            ),
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                }
            }


        }
    }
}