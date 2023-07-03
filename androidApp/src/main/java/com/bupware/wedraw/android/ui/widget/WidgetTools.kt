package com.bupware.wedraw.android.ui.widget

import android.graphics.BitmapFactory
import androidx.compose.runtime.Composable
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.layout.Box
import androidx.glance.layout.fillMaxSize
import com.bupware.wedraw.android.R

@Composable
fun BitmapImageWG(){
    val context = LocalContext.current
    val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.placeholder500)
    Box(modifier = GlanceModifier.fillMaxSize()) {
        Image(
            provider = ImageProvider(bitmap),
            contentDescription = null,
            modifier = GlanceModifier.fillMaxSize()
        )
    }

}