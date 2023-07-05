package com.bupware.wedraw.android.ui.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.OnLifecycleEvent
import com.bupware.wedraw.android.R
import com.bupware.wedraw.android.data.WDDatabase
import com.bupware.wedraw.android.data.tables.image.Image
import com.bupware.wedraw.android.data.tables.image.ImageRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Composable
fun BitmapImageWG(draw: Bitmap) {
    Column(modifier = GlanceModifier.fillMaxSize()) {
        Image(
            provider = ImageProvider(draw),
            contentDescription = null,
            modifier = GlanceModifier.fillMaxSize()
        )
    }

}

@Composable
fun convertBitmapToImageBitmap(bitmap: ImageBitmap): Bitmap {
    return bitmap.asAndroidBitmap()
}


fun getBitmapFromUri(context: Context, uri: Uri): Bitmap? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        val options = BitmapFactory.Options()
        options.inPreferredConfig = Bitmap.Config.ARGB_8888
        BitmapFactory.decodeStream(inputStream, null, options)
    } catch (e: IOException) {
        Log.i("WeDrawWidget", "getBitmapFromUri: IOException ${e.stackTraceToString()}")
        e.printStackTrace()
        null
    }
}




