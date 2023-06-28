package com.bupware.wedraw.android.Components.DrawableBox

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset

class DrawableBoxController(){

    val pathList = "a"

    fun newPath(newPoint: Offset){

    }

}

@Composable
fun rememberDrawableController(): DrawableBoxController = remember {DrawableBoxController()}