package com.bupware.wedraw.android.UI.DrawingScreen

import android.graphics.Bitmap
import android.graphics.Paint
import android.view.MotionEvent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import io.ak1.drawbox.DrawBox
import io.ak1.drawbox.rememberDrawController

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

    val controller = rememberDrawController()

    //Canvas container
    Column() {
        Box(Modifier.height(200.dp)) {
            //DrawableBox(drawableController = rememberDrawableController())
            DrawBox(drawController = controller, bitmapCallback = processImage())
        }

        Column(){
            Button(onClick = {  }) {
                Text(text = "aaa")
            }
        }
    }


}

fun processImage(): (ImageBitmap?, Throwable?) -> Unit {
    return { imageBitmap, error ->
        if (imageBitmap != null) {
            println("ImageBitmap obtenido correctamente: $imageBitmap")
            // Realizar alguna operación con el ImageBitmap obtenido
        } else {
            println("Error al obtener el ImageBitmap: $error")
            // Realizar el manejo de errores correspondiente
        }
    }
}

