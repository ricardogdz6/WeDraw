package com.bupware.wedraw.android.ui.drawingScreen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.util.Log
import android.view.View
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.core.graphics.createBitmap
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import io.ak1.drawbox.DrawBox
import io.ak1.drawbox.DrawBoxPayLoad
import io.ak1.drawbox.rememberDrawController
import kotlinx.coroutines.coroutineScope

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

    var a :DrawBoxPayLoad = DrawBoxPayLoad(Color.Black, emptyList())
    var b: Int
    var context = LocalContext.current
    var scope = rememberCoroutineScope()

    Column() {
        Box(Modifier.height(200.dp)) {
            //DrawableBox(drawableController = rememberDrawableController())
            DrawBox(drawController = controller, bitmapCallback = processImage())
        }

        Column(Modifier.height(IntrinsicSize.Max)){
            Button(onClick = { controller.unDo()  }) {
                Text(text = "undo")
            }

            Button(onClick = { controller.reset()  }) {
                Text(text = "reset")
            }

            Button(onClick = {
                controller.saveBitmap()
                //a = controller.exportPath()
            }) {
                Text(text = "export")
            }

            Button(onClick = { controller.importPath(a)  }) {
                Text(text = "import")
            }


        }

        Column(
            Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .background(Color.White)) {
            Text(text = "")
            Image(bitmap = baba.value, contentDescription = "")
        }


    }
    

}

val baba = mutableStateOf(ImageBitmap(1,1))


fun processImage(): (ImageBitmap?, Throwable?) -> Unit {
    return { imageBitmap, error ->
        if (imageBitmap != null) {
            baba.value = imageBitmap
        } else {
        }
    }
}

