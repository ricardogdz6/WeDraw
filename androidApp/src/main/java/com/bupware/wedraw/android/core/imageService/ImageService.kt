package com.bupware.wedraw.android.core.imageService

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.bupware.wedraw.android.data.WDDatabase
import com.bupware.wedraw.android.data.tables.image.Image
import com.bupware.wedraw.android.data.tables.image.ImageRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resumeWithException

class ImageService @Inject constructor(){

//    @OptIn(ExperimentalCoroutinesApi::class)
//    suspend fun getDrawingImage(context: Context): String = suspendCancellableCoroutine { continuation ->
//        val userDao = WDDatabase.getDatabase(context).imageDao()
//        val repository: ImageRepository = ImageRepository(userDao)
//        repository.getDrawingImage(0)
//
//        val observer = object : Observer<List<Image>> {
//
//
//            override fun onChanged(value: List<Image>) {
//                readAllData.removeObserver(this) // Remove the observer before resuming
//
//                if (value.isNotEmpty()) {
//                    val image = value[0]
//                    Log.i("ARM","Service: "+ image.uri)
//                    continuation.resume(image.uri){}
//                } else {
//                    continuation.resume(""){}
//                }
//            }
//        }
//
//        readAllData.observeForever(observer)
//
//        continuation.invokeOnCancellation {
//            readAllData.removeObserver(observer)
//            continuation.cancel()
//        }
//    }
}

