package com.bupware.wedraw.android.ui.drawingScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
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
//Prueba de rama
    val controller = rememberDrawController()

    //Canvas container
    /*
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
    
     */
    
    Column(Modifier.fillMaxSize()) {

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

