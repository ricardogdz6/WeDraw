package com.bupware.wedraw.android.ui.drawingScreen

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.ImageBitmap
import androidx.core.content.FileProvider
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.saveable
import com.bupware.wedraw.android.data.WDDatabase
import com.bupware.wedraw.android.data.tables.image.Image
import com.bupware.wedraw.android.data.tables.image.ImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.Q)
@HiltViewModel
class DrawingScreenViewModel @Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel() {


    var imageBitmap by savedStateHandle.saveable { mutableStateOf(ImageBitmap(1, 1)) }
    var gameList by savedStateHandle.saveable { mutableStateOf("") }

    var imageBitmapWidget by savedStateHandle.saveable { mutableStateOf(ImageBitmap(1, 1)) }


    fun insertData(context: Context, bitmap: Bitmap) = viewModelScope.launch {
        val imageDao = WDDatabase.getDatabase(context).imageDao()
        val repository: ImageRepository = ImageRepository(imageDao)

        saveImageToCache(context, bitmap, "test")?.let { Image(0, it.toString()) }
            ?.let { repository.insert(it) }
    }

    fun deleteData(context: Context) = viewModelScope.launch {
        val imageDao = WDDatabase.getDatabase(context).imageDao()
        val repository: ImageRepository = ImageRepository(imageDao)
//        repository.deleteAllImages()
    }


    private fun saveImageToExternalStorage(
        bitmap: Bitmap,
        filename: String,
        context: Context
    ): Uri? {
        val resolver = context.contentResolver
        val imageCollection =
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)


        val imageDetails = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, filename)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }

        var imageUri: Uri? = null
        resolver.insert(imageCollection, imageDetails)?.let { uri ->
            resolver.openOutputStream(uri)?.use { outputStream ->
                if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)) {
                    imageUri = uri
                }
            }
        }

        return imageUri
    }

    fun saveImageToCache(context: Context, bitmap: Bitmap, filename: String): Uri? {
        val cacheDirectory = context.cacheDir
        val imageFile = File(cacheDirectory, filename)

        val outputStream = FileOutputStream(imageFile)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.flush()
        outputStream.close()

        // Obtén el URI del archivo de imagen en la caché

        return FileProvider.getUriForFile(
            context,
            "com.bupware.wedraw.android.fileprovider",
            imageFile
        )
    }

    fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }
}